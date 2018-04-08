package io.github.groupease.restendpoint;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import io.github.groupease.auth.CurrentUserId;
import io.github.groupease.db.GroupJoinRequestDao;
import io.github.groupease.model.GroupJoinRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Path("channels/{channelId}/groups/{groupId}/join-requests")
@Produces(MediaType.APPLICATION_JSON)
public class GroupJoinRequestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final GroupJoinRequestDao requestDao;
    private final Provider<String> currentUserIdProvider;

    @Inject
    public GroupJoinRequestService(@Nonnull GroupJoinRequestDao requestDao,
                                   @Nonnull @CurrentUserId Provider<String> currentUserIdProvider)
    {
        this.requestDao = requestDao;
        this.currentUserIdProvider = currentUserIdProvider;
    }

    @GET
    @Timed
    public List<GroupJoinRequest> list(@PathParam("channelId") long channelId, @PathParam("groupId") long groupId)
    {
        LOGGER.debug("GroupJoinRequestService.list()");

        // Make sure that the group is in the channel

        // Make sure that the current user is a member of the group

        return null;
    }

    @GET
    @Path("{invitationId}")
    @Timed
    public GroupJoinRequest get(@PathParam("channelId") long channelId, @PathParam("groupId") long groupId,
                                @PathParam("invitationId") long invitationId)
    {
        LOGGER.debug("GroupJoinRequestService.get()");

        return null;
    }

    @POST
    @Path("{invitationId}/acceptance")
    @Timed
    @Transactional
    public void accept(@PathParam("channelId") long channelId, @PathParam("groupId") long groupId,
                       @PathParam("invitationId") long invitationId)
    {
        LOGGER.debug("GroupJoinRequestService.accept()");
    }

    @POST
    @Path("{invitationId}/rejection")
    @Timed
    @Transactional
    public void reject(@PathParam("channelId") long channelId, @PathParam("groupId") long groupId,
                       @PathParam("invitationId") long invitationId)
    {
        LOGGER.debug("GroupJoinRequestService.reject()");
    }

    @POST
    @Timed
    @Transactional
    public GroupJoinRequest create(@PathParam("channelId") long channelId, @PathParam("groupId") long groupId)
    {
        LOGGER.debug("GroupJoinRequestService.create()");

        return null;
    }

    @DELETE
    @Path("{invitationId}")
    @Timed
    @Transactional
    public void delete(@PathParam("channelId") long channelId, @PathParam("groupId") long groupId,
                       @PathParam("invitationId") long invitationId)
    {
        LOGGER.debug("GroupJoinRequestService.delete()");
    }
}
