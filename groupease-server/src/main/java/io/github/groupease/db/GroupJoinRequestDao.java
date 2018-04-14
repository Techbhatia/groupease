package io.github.groupease.db;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import io.github.groupease.model.Group;
import io.github.groupease.model.GroupJoinRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.invoke.MethodHandles;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class GroupJoinRequestDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EntityManager entityManager;

    /**
     * Injectable constructor.
     *
     * @param entityManager
     */
    @Inject
    public GroupJoinRequestDao(@Nonnull EntityManager entityManager)
    {
        this.entityManager = requireNonNull(entityManager);
    }

    /**
     * Gets a list of all {@link GroupJoinRequest}s sent to a group
     * @param groupId The unique ID of the group to get requests for
     * @return The list of requests for the group. If there are none, the list will be empty
     */
    public List<GroupJoinRequest> list(long groupId)
    {
        LOGGER.debug("GroupJoinRequestDao.list({})", groupId);

        TypedQuery<GroupJoinRequest> query = entityManager.createQuery(
                "SELECT gjr FROM GroupJoinRequest gjr WHERE gjr.group.id = :groupId", GroupJoinRequest.class);
        query.setParameter("groupId", groupId);

        return query.getResultList();
    }

    /**
     * Gets a list of any {@link GroupJoinRequest}s sent to a group from a particular user
     * @param groupId The unique of the group to get requests for
     * @param senderId The user to filter the list by
     * @return The list of requests for the group from the user. If there are none, the list will be empty
     */
    public List<GroupJoinRequest> list(long groupId, long senderId)
    {
        LOGGER.debug("GroupJoinRequestDao.list(group={}, sender={})", groupId, senderId);

        TypedQuery<GroupJoinRequest> query = entityManager.createQuery(
                "SELECT gjr FROM GroupJoinRequest gjr WHERE gjr.group.id = :groupId AND gjr.sender.id = :senderId",
                GroupJoinRequest.class);
        query.setParameter("groupId", groupId);
        query.setParameter("senderId", senderId);

        return query.getResultList();
    }

    /**
     * Gets a specific {@link GroupJoinRequest} but only if it is for a specific group in a specific channel
     * @param channelId The unique ID of the channel that the group that the request is sent to must be in
     * @param groupId The unique ID of the group that the request is sent to
     * @param requestId The unique ID of the join request
     * @return The specified join request if found and sent to the right group in the right channel
     */
    public GroupJoinRequest get(long channelId, long groupId, long requestId)
    {
        LOGGER.debug("GroupJoinRequestDao.get(channel={}, group={}, id={})", channelId, groupId, requestId);

        TypedQuery<GroupJoinRequest> query = entityManager.createQuery(
                "SELECT gjr FROM GroupJoinRequest gjr WHERE gjr.group.channelId = :channelId AND gjr.group.id = :groupId AND gjr.id = :requestId",
                GroupJoinRequest.class);
        query.setParameter("channelId", channelId);
        query.setParameter("groupId", groupId);
        query.setParameter("requestId", requestId);

        List<GroupJoinRequest> result = query.getResultList();
        if(result.isEmpty())
        {
            return null;
        }

        return result.get(0);
    }

    /**
     * Removes a {@link GroupJoinRequest} from the database
     * @param request The request to remove. It must have been previously retrieved and not constructed
     */
    @Transactional
    public void delete(@Nonnull GroupJoinRequest request)
    {
        LOGGER.debug("GroupJoinRequestDao.delete({})", request.getId());

        entityManager.remove(request);
    }
}
