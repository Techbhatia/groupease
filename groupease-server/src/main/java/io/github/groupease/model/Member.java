package io.github.groupease.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.annotation.Nonnull;
import javax.persistence.*;

@Entity
@Table(name="Member")
public class Member implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isOwner;

    // Issue #31 - Add additional per-channel profile fields here once determined

    @UpdateTimestamp
    private Instant lastUpdate;

    @ManyToOne
    @JoinColumn(name = "ChannelID", referencedColumnName = "ID")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "ID")
    private GroupeaseUser userProfile;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private List<Group> groups;

    // Accessor functions

    /**
     * Gets the unique ID of this membership relationship
     *
     * @return The unique ID
     */
    public Long getId() { return id; }

    /**
     * Gets whether the member is an owner of the channel
     *
     * @return True when the member is an owner, otherwise false
     */
    public boolean isOwner() { return isOwner; }

    /**
     * Gets the {@link GroupeaseUser} that is a member of this channel
     *
     * @return The user profile object
     */
    public GroupeaseUser getGroupeaseUser() {
        return userProfile;
    }

    /**
     * Gets the Channel that the user is a member of
     *
     * @return The Channel object
     */
    @JsonIgnore
    public Channel getChannel() {
        return channel;
    }

    /**
     * Gets the last time the persisted version of this membership was changed
     *
     * @return The last update time
     */
    public Instant getLastUpdate() {
        return lastUpdate;
    }

    // Setter functions
    public void setGroupeaseUser(@Nonnull GroupeaseUser userProfile)
    {
        this.userProfile = userProfile;
    }

    public void setChannel(@Nonnull Channel channel)
    {
        this.channel = channel;
    }
}
