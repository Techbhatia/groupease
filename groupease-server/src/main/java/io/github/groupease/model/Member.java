package io.github.groupease.model;

<<<<<<< HEAD
=======
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.UpdateTimestamp;

>>>>>>> changes
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="Member")
public class Member implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isOwner;

    /* TODO: Issue #31 - Additional per-channel profile fields. */
    private String skills;
    private String availability;
    private String goals;

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

    public String getSkills() {
        return skills;
    }

    public String getGoals() {
        return goals;
    }

    public String getAvailability() {
        return availability;
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
<<<<<<< HEAD

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Override
    @Nonnull
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

=======
>>>>>>> changes
}
