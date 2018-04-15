package io.github.groupease.restendpoint;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.persist.Transactional;
import io.github.groupease.auth.CurrentUserId;
import io.github.groupease.channel.ChannelNotFoundException;
import io.github.groupease.db.GroupDao;
import io.github.groupease.db.GroupInvitationDao;
import io.github.groupease.db.GroupeaseUserDao;
import io.github.groupease.exception.*;
import io.github.groupease.model.*;
import io.github.groupease.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Path("users/{userId}/channels/{channelId}/group-invitations")
@Produces(MediaType.APPLICATION_JSON)
public class GroupInvitationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final GroupInvitationDao invitationDao;
    private final GroupeaseUserDao userDao;
    private final GroupDao groupDao;
    private final Provider<String> currentUserIdProvider;
    private GroupeaseUser loggedOnUser;

    @Inject
    public GroupInvitationService(@Nonnull GroupInvitationDao invitationDao, @Nonnull GroupeaseUserDao userDao,
                                  @Nonnull GroupDao groupDao,
                                  @Nonnull @CurrentUserId Provider<String> currentUserIdProvider)
    {
        this.currentUserIdProvider = currentUserIdProvider;
        this.userDao = userDao;
        this.groupDao = groupDao;
        this.invitationDao = invitationDao;
    }

    /**
     * Returns all {@link GroupInvitation}s for a user
     * @param userId The ID of the user to retrieve invitations for
     * @param channelId The ID of the channel to retrieve invitations for
     * @return The list of invitations for the user. The list will be empty if there are no invitations
     */
    @GET
    @Timed
    public List<GroupInvitation> list(@PathParam("userId") long userId, @PathParam("channelId") long channelId)
    {
        LOGGER.debug("GroupInvitationService.list(user={}, channel={})", userId, channelId);

        verifyLoggedInUser(userId, channelId);

        return invitationDao.list(userId, channelId);
    }

    /**
     * Gets a specific {@link GroupInvitation}
     * @param userId The ID of the user the invitation was sent to
     * @param channelId The ID of the channel the group being invited to is in
     * @param invitationId The ID of the invitation
     * @return The specified invitation
     */
    @GET
    @Path("{invitationId}")
    @Timed
    public GroupInvitation get(@PathParam("userId") long userId, @PathParam("channelId") long channelId,
                               @PathParam("invitationId") long invitationId)
    {
        LOGGER.debug("GroupInvitationService.get(user={}, invitation={})", userId, invitationId);

        verifyLoggedInUser(userId, channelId);

        GroupInvitation invitation = invitationDao.get(invitationId, userId, channelId);
        if(invitation == null)
        {
            throw new GroupInvitationNotFoundException(
                    "No invitation with that ID in that channel for that user exists");
        }

        return invitation;
    }

    /**
     * Creates (sends) a new {@link GroupInvitation} inviting a user to join a group. Only an existing group member
     * can send an invitation
     * @param userId The ID of the user being invited
     * @param channelId The ID of the channel the group being invited to is in
     * @param invitation A {@link GroupInvitation} that contains the posted JSON with the user and group IDs
     * to create the invitation for
     * @return The newly created invitation.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    @Transactional
    public GroupInvitation create(@PathParam("userId") long userId, @PathParam("channelId") long channelId,
                                  @Nonnull GroupInvitation invitation)
    {
        LOGGER.debug("GroupInvitationService.create(userUrl={}, channelUrl={})",
                userId, channelId);

        // Group must be provided in POSTed JSON
        if(invitation.getGroup() == null || invitation.getGroup().getId() == null)
        {
            throw new GroupIdMissingException("Group id must be supplied in request");
        }

        // Recipient ID is optional in the JSON, but if supplied it must match the URL
        if(invitation.getRecipient() != null && invitation.getRecipient().getId() != userId)
        {
            throw new UserMismatchException("The user in the path and the user in the JSON don't match");
        }

        // Ensure that the specified group exists and is part of the channel in the URL
        Group targetGroup = groupDao.get(invitation.getGroup().getId());
        if(targetGroup == null)
        {
            throw new GroupNotFoundException("No group with that ID found");
        }
        if(targetGroup.getChannelId() != channelId)
        {
            throw new ChannelNotFoundException("Channel not found");
        }

        // Only a current group member can create an invitation
        loggedOnUser = userDao.getByProviderId(currentUserIdProvider.get());
        if(targetGroup.getMembers().stream().noneMatch(member -> member.getGroupeaseUser().equals(loggedOnUser)))
        {
            throw new NotGroupMemberException("You must be a group member to send an invitation");
        }

        // Verify that the recipient exists
        GroupeaseUser recipientUser = userDao.getById(userId);
        if(recipientUser == null)
        {
            throw new UserNotFoundException("You cannot invite a user that does not exist");
        }

        // Verify the recipient is a member of the channel
        if(recipientUser.getMemberList()
                .stream().noneMatch(member -> member.getChannel().getId() == targetGroup.getChannelId()))
        {
            throw new NotChannelMemberException(
                    "You cannot invite a user that is not a member of the channel that the group is formed in");
        }

        // Check that the recipient hasn't already received an invitation to this group
        GroupInvitation existingInvitation = invitationDao.get(userId, invitation.getGroup().getId());
        if(existingInvitation != null)
        {
            // If there is already an invitation, don't create another one. Return the existing one as a reminder
            return existingInvitation;
        }

        // Verify that the recipient isn't already a group member
        if(targetGroup.getMembers().stream().anyMatch(member -> member.getGroupeaseUser().equals(recipientUser)))
        {
            throw new AlreadyMemberException("Cannot invite a user to a group the user is already a member of");
        }

        return invitationDao.create(loggedOnUser, recipientUser, targetGroup);
    }

    /**
     * Processes a user's acceptance of an invitation to join a group. The user will be added as a member of the group
     * and the {@link GroupInvitation} will be deleted
     * @param userId The ID of the user that the invitation was sent to
     * @param channelId The ID of the channel the group being invited to is in
     * @param invitationId The ID of the invitation being accepted
     */
    @POST
    @Path("{invitationId}/acceptance")
    @Timed
    @Transactional
    public void accept(@PathParam("userId") long userId, @PathParam("channelId") long channelId,
                       @PathParam("invitationId") long invitationId)
    {
        LOGGER.debug("GroupInvitationService.accept({})", invitationId);

        verifyLoggedInUser(userId, channelId);

        // Check that the invitation exists and verify the invitation is for this user
        GroupInvitation invitation = invitationDao.get(invitationId, userId, channelId);
        if(invitation == null)
        {
            throw new GroupInvitationNotFoundException(
                    "No invitation with that ID in that channel for that user exists");
        }

        // Add the member to the group
        invitation.getGroup().getMembers().add(invitation.getRecipient().getMemberList()
                .stream().filter(member -> member.getChannel().getId() == channelId).findFirst().get());

        // Clean up the invitation
        invitationDao.delete(invitation);
    }

    /**
     * Processes a user's rejection of an invitation to join a group. The {@link GroupInvitation} will be deleted
     * @param userId The ID of the user that the invitation was sent to
     * @param channelId The ID of the channel the group being invited to is in
     * @param invitationId The ID of the invitation
     */
    @POST
    @Path("{invitationId}/rejection")
    @Timed
    @Transactional
    public void reject(@PathParam("userId") long userId, @PathParam("channelId") long channelId,
                       @PathParam("invitationId") long invitationId)
    {
        LOGGER.debug("GroupInvitationService.reject({})", invitationId);

        verifyLoggedInUser(userId, channelId);

        // Check that the invitation exists and verify the invitation is for this user
        GroupInvitation invitation = invitationDao.get(invitationId, userId, channelId);
        if(invitation == null)
        {
            throw new GroupInvitationNotFoundException(
                    "No invitation with that ID in that channel for that user exists");
        }

        // Clean up the invitation
        invitationDao.delete(invitation);
    }

    /**
     * Deletes (cancels) a {@link GroupInvitation}. Only an existing group member may delete an invitation
     * @param userId The ID of the user the invitation was sent to
     * @param channelId The ID of the channel the group being invited to is in
     * @param invitationId The ID of the invitation
     */
    @DELETE
    @Path("{invitationId}")
    @Timed
    @Transactional
    public void delete(@PathParam("userId") long userId, @PathParam("channelId") long channelId,
                       @PathParam("invitationId") long invitationId)
    {
        LOGGER.debug("GroupInvitationService.delete(user={}, channel={}, invitation={})",
                userId, channelId, invitationId);

        // Make sure this invitation actually exists and is for the given user in the given channel
        GroupInvitation invitation = invitationDao.get(invitationId, userId, channelId);
        if(invitation == null)
        {
            throw new GroupInvitationNotFoundException(
                    "No invitation with that ID in that channel for that user exists");
        }

        // Validate that the current user is already a group member
        loggedOnUser = userDao.getByProviderId(currentUserIdProvider.get());

        if(invitation.getGroup().getMembers()
                .stream().noneMatch(member -> member.getGroupeaseUser().equals(loggedOnUser)))
        {
            throw new NotGroupMemberException("Only a group member can delete the request. Recipient reject instead");
        }

        invitationDao.delete(invitation);
    }

    /**
     * Helper method to verify the logged in user matches the user ID in the URL and that the user is a member
     * of the channel specified in the URL. Any violation will throw an appropriate exception
     * @param userId The user ID from the URL
     * @param channelId The channel ID from the URL
     */
    private void verifyLoggedInUser(long userId, long channelId)
    {
        if(loggedOnUser == null)
        {
            loggedOnUser = userDao.getByProviderId(currentUserIdProvider.get());
            if(loggedOnUser == null)
            {
                LOGGER.debug("verifyLoggedInUser({}): No profile for logged on user in database", userId);
                throw new UserNotFoundException("Currently logged in user has no profile");
            }
        }
        if(loggedOnUser.getId() != userId)
        {
            throw new UserMismatchException("Logged in user does not match the provided user id");
        }
        
        if(loggedOnUser.getMemberList().stream().noneMatch(member -> member.getChannel().getId() == channelId))
        {
            throw new NotChannelMemberException(
                    "You cannot perform operations in this channel because you are not a member");
        }
    }
}
