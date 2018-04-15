package io.github.groupease.channel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Business service for operations on {@link Channel} instances.
 */
public interface ChannelService {

    /**
     * Fetch all {@link Channel} instances in the system.
     *
     * @return the list of all {@link Channel} instances.
     */
    @Nonnull
    List<Channel> list();

    /**
     * Fetch all {@link Channel} instances that Member is a part of.
     *
     * @return the list of all {@link Channel} instances that include the memberId.
     */
    @Nonnull
    List<Channel> list(
            long userId
    );

    /**
     * Fetch a {@link Channel} instance by its ID.
     *
     * @param id the ID of the {@link Channel} instance to fetch.
     * @return the matching {@link Channel} instance.
     */
    @Nonnull
    Channel getById(
            long id
    );

    /**
     * Save updates to an existing {@link Channel} instance.
     *
     * @param toUpdate the {@link ChannelDto} instance to save.
     * @return the updated {@link Channel} instance.
     */
    @Nonnull
    Channel update(
            @Nonnull ChannelDto toUpdate
    );

    /**
     * Create a new {@link Channel} instance.
     *
     * @param toCreate the {@link ChannelDto} instance to create.
     * @return the newly created {@link Channel} instance.
     */
    @Nonnull
    Channel create(
            @Nonnull ChannelDto toCreate
    );

    /**
     * Delete an existing {@link Channel} instance.
     *
     * @param id the ID of the {@link Channel} instance to delete.
     */
    void delete(
            long id
    );

}
