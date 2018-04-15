package io.github.groupease.db;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import static java.util.Objects.requireNonNull;

import com.google.inject.persist.Transactional;
import io.github.groupease.model.Group;
import io.github.groupease.model.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class GroupDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EntityManager entityManager;
    private EntityTransaction updateTransaction;

    /**
     * Injectable constructor.
     *
     * @param entityManager
     */
    @Inject
    public GroupDao(@Nonnull EntityManager entityManager)
    {
        this.entityManager = requireNonNull(entityManager);
    }

    /**
     * Gets a list of all {@link Group}s in a channel
     * @param channelId The ID of the channel
     * @return The list of groups. The list is empty if none are found
     */
    @Nonnull
    public List<Group> list(long channelId) {
        LOGGER.debug("GroupDao.listInChannel({}) called", channelId);

        TypedQuery<Group> query = entityManager.createQuery(
                "SELECT group FROM Group group WHERE group.channelId = ?1 ORDER BY group.name ASC",
                Group.class);
        query.setParameter(1, channelId);

        return query.getResultList();
    }

    /**
     * Gets a specific {@link Group} by ID
     * @param id The ID of the group
     * @return The specified group or null if no group with the given ID exists
     */
    public Group get(long id)
    {
        LOGGER.debug("GroupDao.getById({}) called", id);

        return entityManager.find(Group.class, id);
    }

    /**
     * Gets a {@link Group} with a given name in a channel
     * @param name the name of the group
     * @param channelId The ID of the channel to search in
     * @return The specified group or null if no matching group was found
     */
    public Group get(String name, long channelId)
    {
        LOGGER.debug("GroupDao.getByNameAndChannel(name={}, channel={})", name, channelId);

        TypedQuery<Group> query = entityManager.createQuery(
                "SELECT g FROM Group g WHERE g.channelId=?1 AND g.name=?2", Group.class);
        query.setParameter(1, channelId);
        query.setParameter(2, name);

        List<Group> results = query.getResultList();
        if(results.isEmpty())
        {
            return null;
        }

        return results.get(0);
    }

    /**
     * Deletes a {@link Group} from the database
     * @param group The group to delete. It must have been previously retrieved and not manually constructed
     */
    @Transactional
    public void delete(@Nonnull Group group)
    {
        LOGGER.debug("GroupDao.delete({}) called", group.getName());

        entityManager.remove(group);
    }

    /**
     * Creates a new {@link Group} stored in the database
     * @param channelId The ID of the channel to store it in
     * @param name The name of the new group
     * @param description An optional description of the group
     * @return The newly created group object complete with unique ID
     */
    @Transactional
    public Group create(long channelId, @Nonnull String name, String description, Member firstMember)
    {
        LOGGER.debug("GroupDao.Create(channelId={}, name={}, description={})", channelId, name, description);

        Group newGroup = new Group(channelId, name, description);

        newGroup.getMembers().add(firstMember);
        entityManager.persist(newGroup);

        return newGroup;
    }
}
