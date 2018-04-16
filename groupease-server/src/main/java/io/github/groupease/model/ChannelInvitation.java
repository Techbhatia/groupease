package io.github.groupease.model;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name="ChannelInvitation")
public class ChannelInvitation {
    @Id
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "ChannelID", referencedColumnName = "ID")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "SenderID", referencedColumnName = "ID")
    private GroupeaseUser sentBy;

    @ManyToOne
    @JoinColumn(name = "RecipientID", referencedColumnName = "ID")
    private GroupeaseUser recipient;

    // Not currently in API documentation so commenting out for the time being
    //private String comments;

    @UpdateTimestamp
    private Instant lastUpdate;

    // Accessor methods

    /**
     * Gets the unique ID of this invitation
     * @return The unique ID
     */
    public Long getId() { return id; }

    /**
     * Gets the {@link GroupeaseUser} that receives this invitation
     * @return The recipient
     */
    public GroupeaseUser getRecipient() {
        return recipient;
    }

    /**
     * Gets the {@link GroupeaseUser} that sent this invitation
     * @return The sender of the invitation
     */
    public GroupeaseUser getSentBy() {
        return sentBy;
    }

    /**
     * Gets the channel the recipient is being invited to
     * @return The channel the recipient is being invited to
     */
    public Channel getChannel()
    {
        return channel;
    }

    /*public String getComments() {
        return comments;
    }*/

    /**
     * Gets the last time the persisted version of this invitation was modified
     * @return The last persisted update time
     */
    public Instant getLastUpdate() {
        return lastUpdate;
    }
}
