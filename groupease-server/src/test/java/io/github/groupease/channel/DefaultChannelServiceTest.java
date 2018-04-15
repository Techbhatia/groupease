package io.github.groupease.channel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.common.collect.ImmutableList;
import io.github.groupease.GroupeaseTestGuiceModule;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Unit tests for {@link DefaultChannelService}.
 */
@Guice(modules = GroupeaseTestGuiceModule.class)
public class DefaultChannelServiceTest {

    @Inject
    private ChannelDao channelDao;

    @Inject
    private Channel channel;

    @Inject
    private Provider<ChannelDto> channelDtoProvider;

    @Inject
    private Provider<DefaultChannelService> toTestProvider;

    private DefaultChannelService toTest;

    private ChannelDto channelDto;

    /**
     * Set up tests.
     *
     * @throws Exception on error.
     */
    @BeforeMethod
    public void setUp() throws Exception {
        /* Reset all injected mocks between tests. */
        reset(channelDao);

        /* Get new instance of ChannelDto for each test since it is mutable. */
        channelDto = channelDtoProvider.get();

        /* Get instance to test. */
        toTest = toTestProvider.get();
    }

    /**
     * It should call {@link ChannelDao#list()} and return the result.
     *
     * @throws Exception on error.
     */
    @Test
    public void testList() throws Exception {
        /* Set up test. */
        List<Channel> expected = ImmutableList.of(channel);

        /* Train the mocks. */
        when(channelDao.list()).thenReturn(expected);

        /* Make the call. */
        List<Channel> actual = toTest.list();

        /* Verify results. */
        assertEquals(actual, expected);
    }

    /**
     * It should call {@link ChannelDao#getById(long)} and return the result.
     *
     * @throws Exception on error.
     */
    @Test
    public void testGetById() throws Exception {
        /* Set up test. */
        Channel expected = channel;

        /* Train the mocks. */
        when(channelDao.getById(channel.getId())).thenReturn(expected);

        /* Make the call. */
        Channel actual = toTest.getById(channel.getId());

        /* Verify results. */
        assertEquals(actual, expected);
    }

    /**
     * It should call {@link ChannelDao#update(ChannelDto)} and return the result.
     *
     * @throws Exception on error.
     */
    @Test
    public void testUpdate() throws Exception {
        /* Set up test. */
        Channel expected = channel;

        /* Train the mocks. */
        when(channelDao.getById(channelDto.getId())).thenReturn(expected);
        when(channelDao.update(channelDto)).thenReturn(expected);

        /* Make the call. */
        Channel actual = toTest.update(channelDto);

        /* Verify results. */
        assertEquals(actual, expected);
    }

    /**
     * It should throw {@link ChannelNotFoundException} when channel to update is not found.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = ChannelNotFoundException.class)
    public void testUpdateWhenNotFound() throws Exception {
        /* Train the mocks. */
        when(channelDao.getById(channelDto.getId())).thenThrow(ChannelNotFoundException.class);

        /* Make the call. */
        toTest.update(channelDto);
    }

    /**
     * It should throw {@link NullPointerException} when input is null.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testUpdateWhenDtoNull() throws Exception {
        /* Make the call. */
        toTest.update(null);
    }

    /**
     * It should throw {@link ChannelNameMissingException} when name is null.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = ChannelNameMissingException.class)
    public void testUpdateWhenNameNull() throws Exception {
        /* Set up test. */
        channelDto.setName(null);

        /* Make the call. */
        toTest.update(channelDto);
    }

    /**
     * It should throw {@link ChannelNameMissingException} when name is blank.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = ChannelNameMissingException.class)
    public void testUpdateWhenNameBlank() throws Exception {
        /* Set up test. */
        channelDto.setName("");

        /* Make the call. */
        toTest.update(channelDto);
    }

    /**
     * It should call {@link ChannelDao#create(ChannelDto)} and return the result.
     *
     * @throws Exception on error.
     */
    @Test
    public void testCreate() throws Exception {
        /* Set up test. */
        Channel expected = channel;

        channelDto.setId(null);

        /* Train the mocks. */
        when(channelDao.create(channelDto)).thenReturn(expected);

        /* Make the call. */
        Channel actual = toTest.create(channelDto);

        /* Verify results. */
        assertEquals(actual, expected);
    }

    /**
     * It should throw {@link NewChannelHasIdException} when channel to create already has an ID.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = NewChannelHasIdException.class)
    public void testCreateAlreadyHasId() throws Exception {
        /* Make the call. */
        toTest.create(channelDto);
    }

    /**
     * It should throw {@link NullPointerException} when input is null.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testCreateWhenDtoNull() throws Exception {
        /* Make the call. */
        toTest.create(null);
    }

    /**
     * It should throw {@link ChannelNameMissingException} when name is null.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = ChannelNameMissingException.class)
    public void testCreateWhenNameNull() throws Exception {
        /* Set up test. */
        channelDto.setId(null);
        channelDto.setName(null);

        /* Make the call. */
        toTest.create(channelDto);
    }

    /**
     * It should throw {@link ChannelNameMissingException} when name is blank.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = ChannelNameMissingException.class)
    public void testCreateWhenNameBlank() throws Exception {
        /* Set up test. */
        channelDto.setId(null);
        channelDto.setName("");

        /* Make the call. */
        toTest.create(channelDto);
    }

    /**
     * It should call {@link ChannelDao#delete(long)} with the provided ID.
     *
     * @throws Exception on error.
     */
    @Test
    public void testDelete() throws Exception {
        /* Train the mocks. */
        when(channelDao.getById(channelDto.getId())).thenReturn(channel);

        /* Make the call. */
        toTest.delete(channel.getId());

        /* Verify results. */
        verify(channelDao).delete(channel.getId());
    }

    /**
     * It should throw {@link ChannelNotFoundException} when channel to delete is not found.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = ChannelNotFoundException.class)
    public void testDeleteWhenNotFound() throws Exception {
        /* Train the mocks. */
        when(channelDao.getById(channelDto.getId())).thenThrow(ChannelNotFoundException.class);

        /* Make the call. */
        toTest.delete(channel.getId());
    }

}
