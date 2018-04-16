package io.github.groupease.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name="GroupInvitation")
public class GroupInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GroupID", referencedColumnName = "ID")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "SenderID", referencedColumnName = "ID")
    private GroupeaseUser sender;

    @ManyToOne
    @JoinColumn(name = "RecipientID", referencedColumnName = "ID")
    private GroupeaseUser recipient;

    private String comments;

    @UpdateTimestamp
    private Instant lastUpdate;

    public GroupInvitation() {}

    public GroupInvitation(GroupeaseUser sender, GroupeaseUser recipient, Group group)
    {
        this.sender = sender;
        this.recipient = recipient;
        this.group = group;
    }

    @Override
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Gets the unique ID of this invitation
     * @return The unique ID of this invitation
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Gets the {@link Group} that the user is being invited to
     * @return The group the user is being invited to
     */
    public Group getGroup()
    {
        return group;
    }

    /**
     * Gets the {@link GroupeaseUser} that sent the invitation
     * @return The user that sent the invitation
     */
    public GroupeaseUser getSender() {
        return sender;
    }

    /**
     * Gets the {@link GroupeaseUser} that is being invited to the group
     * @return The user that is being invited to the group
     */
    public GroupeaseUser getRecipient() {
        return recipient;
    }

    /**
     * Gets the last time the persisted version this invitation was modified
     * @return The last time the persisted version this invitation was modified
     */
    public Instant getLastUpdate() {
        return lastUpdate;
    }
}
