package io.github.groupease.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.persistence.*;

/**
 * Represents a Group being formed in a Channel (course section)
 */
@Entity
@Table(name = "ChannelGroup")
public final class Group implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long channelId;

    @Nonnull
    private String name;
    private String description;

    // Not currently used, but keeping placeholder for later
    private boolean isFull;

    @UpdateTimestamp
    private Instant lastUpdate;

    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="GroupMember", inverseJoinColumns = @JoinColumn(name="MemberID"),
            joinColumns = @JoinColumn(name = "GroupID"))
    private List<Member> members = new ArrayList<Member>();

    // Constructors
    public Group() { isFull = false; }

    public Group(long channelId, @Nonnull String name, String description)
    {
        this.channelId = channelId;
        this.name = name;
        this.description = description;
        this.isFull = false;
    }

    // Accessor methods

    /**
     * Gets the unique identifier for the group
     * @return The unique identifier for the group
     */
    public Long getId() { return id; }

    /**
     * Get the ID of the channel this group is in
     * @return the ID of the channel this group is in
     */
    public long getChannelId() { return channelId; }

    /**
     * Gets the name of the group
     * @return the name of the group
     */
    @Nonnull
    public String getName() { return name; }

    /**
     * Gets the description of the group, if any
     * @return the description of the group
     */
    public String getDescription() { return description; }

    /**
     * Indicates whether the group is full and cannot accept additional members
     * @return True when the group is full
     */
    @JsonIgnore
    public boolean isFull() { return isFull; }

    /**
     * Gets the last time the persisted version of this object was modified
     * @return the last time the persisted version of this object was modified
     */
    public Instant getLastUpdate() { return lastUpdate; }

    /**
     * Gets the list of {@link Member}s in this group
     * @return The list of group members
     */
    public List<Member> getMembers() { return members; }

    // Setter methods
    public void setName(String newName) { name = newName; }
    public void setDescription(String newDescription) { description = newDescription; }
}
