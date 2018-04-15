package io.github.groupease.channel;

import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * REST-ful web service for {@link Channel} instances.
 */
@Path("channels")
@Produces(MediaType.APPLICATION_JSON)
@Immutable
public class ChannelWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ChannelService channelService;

    /**
     * Injectable constructor.
     *
     * @param channelService logic service layer for {@link Channel} operations.
     */
    @Inject
    public ChannelWebService(
            @Nonnull ChannelService channelService
    ) {
        this.channelService = requireNonNull(channelService);
    }

    /**
     * Fetch all {@link Channel} instances in the system.
     *
     * @return the list of all {@link Channel} instances.
     */
    @GET
    @Timed
    @Nonnull
    public List<Channel> list(
            @QueryParam("userId") Long userId
    ) {
        LOGGER.debug("ChannelWebService.list() called with memberId: '{}'.", userId);
        if (userId != null) {
            return channelService.list(userId);
        } else 
            return channelService.list();
    }

    /**
     * Fetch a {@link Channel} instance by its ID.
     *
     * @param id the ID of the {@link Channel} instance to fetch.
     * @return the matching {@link Channel} instance.
     */
    @GET
    @Path("{id}")
    @Timed
    @Nonnull
    public Channel getById(
            @PathParam("id") Long id
    ) {
        LOGGER.debug("ChannelWebService.getById({}) called.", id);
        return channelService.getById(id);
    }

    /**
     * Save updates to an existing {@link Channel} instance.
     *
     * @param id the ID of the {@link Channel} instance to update.
     * @param toUpdate the {@link ChannelDto} instance to save.
     * @return the updated {@link Channel} instance.
     * @throws IllegalArgumentException if id and toUpdate.getId() do NOT match.
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    @Nonnull
    public Channel update(
            @PathParam("id") Long id,
            @Nonnull ChannelDto toUpdate
    ) {
        LOGGER.debug("ChannelWebService.update({}, {}) called.", id, toUpdate);

        if (id.equals(toUpdate.getId())) {
            return channelService.update(toUpdate);
        }

        throw new ChannelIdMismatchException("Provided Channel ID does not match URL ID.");
    }

    /**
     * Create a new {@link Channel} instance.
     *
     * @param toCreate the {@link ChannelDto} instance to create.
     * @return the newly created {@link Channel} instance.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    @Nonnull
    public Channel create(
            @Nonnull ChannelDto toCreate
    ) {
        LOGGER.debug("ChannelWebService.create({}) called.", toCreate);
        return channelService.create(toCreate);
    }

    /**
     * Delete an existing {@link Channel} instance.
     *
     * @param id the ID of the {@link Channel} instance to delete.
     */
    @DELETE
    @Path("{id}")
    @Timed
    public void delete(
            @PathParam("id") Long id
    ) {
        LOGGER.debug("ChannelWebService.delete({}) called.", id);
        channelService.delete(id);
    }

}
