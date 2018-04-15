package io.github.groupease.channel;

import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Strings;
import com.google.inject.persist.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of {@link ChannelService}.
 */
@Immutable
public class DefaultChannelService implements ChannelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ChannelDao channelDao;

    /**
     * Injectable constructor.
     *
     * @param channelDao {@link ChannelDao} instance.
     */
    @Inject
    public DefaultChannelService(
            @Nonnull ChannelDao channelDao
    ) {
        this.channelDao = requireNonNull(channelDao);
    }

    @Nonnull
    @Override
    @Timed
    public List<Channel> list() {
        LOGGER.debug("DefaultChannelService.list() called.");
        return channelDao.list();
    }

    @Nonnull
    @Override
    @Timed
    public List<Channel> list(
            long userId
    ) {
        LOGGER.debug("DefaultChannelService.list({}) called.", userId);
        return channelDao.list(userId);
    }

    @Nonnull
    @Override
    @Timed
    public Channel getById(
            long id
    ) {
        LOGGER.debug("DefaultChannelService.getById({}) called.", id);
        return channelDao.getById(id);
    }

    @Nonnull
    @Override
    @Timed
    @Transactional
    public Channel update(
            @Nonnull ChannelDto toUpdate
    ) {
        LOGGER.debug("DefaultChannelService.update({}) called.", toUpdate);

        requireNonNull(toUpdate);

        if (Strings.isNullOrEmpty(toUpdate.getName())) {
            throw new ChannelNameMissingException("Channel name is required.");
        }

        /* Confirm toUpdate exists and current user has access. */
        channelDao.getById(toUpdate.getId());

        return channelDao.update(toUpdate);
    }

    @Nonnull
    @Override
    @Timed
    @Transactional
    public Channel create(
            @Nonnull ChannelDto toCreate
    ) {
        LOGGER.debug("DefaultChannelService.create({}) called.", toCreate);

        requireNonNull(toCreate);

        if (toCreate.getId() != null) {
            throw new NewChannelHasIdException("A new Channel cannot already have an ID.");
        }

        if (Strings.isNullOrEmpty(toCreate.getName())) {
            throw new ChannelNameMissingException("Channel name is required.");
        }

        return channelDao.create(toCreate);
    }

    @Override
    @Timed
    @Transactional
    public void delete(
            long id
    ) {
        LOGGER.debug("DefaultChannelService.delete({}) called.", id);

        /* Confirm channel to delete exists and current user has access. */
        channelDao.getById(id);

        channelDao.delete(id);
    }

}
