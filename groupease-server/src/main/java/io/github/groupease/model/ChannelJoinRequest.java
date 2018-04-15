package io.github.groupease.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name="ChannelJoinRequest")
public class ChannelJoinRequest {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ChannelID", referencedColumnName = "ID")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "ID")
    private GroupeaseUser requestor;

    private String comments;

    @UpdateTimestamp
    private Instant lastUpdate;

    // Accessor methods

    /**
     * Gets the ID of this request
     *
     * @return The ID of this request
     */
    public Long getId() { return id; }

    /**
     * Helper method to get the ID of the associated {@link Channel}
     *
     * @return The channel ID
     */
    public long getChannelId() { return channel.getId(); }

    /**
     * Gets the user supplied comments justifying this request
     *
     * @return the user supplied comments
     */
    public String getComments() { return comments; }

    /**
     * Gets the time this requst was last modified
     *
     * @return The modification time
     */
    public Instant getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Gets the {@link GroupeaseUser} associated with the user that submitted the request
     *
     * @return The profile of the user that submitted the request
     */
    public GroupeaseUser getRequestor() {
        return requestor;
    }

    /**
     * Gets the {@link Channel} that the user is requesting access to
     *
     * @return The channel requesting access to
     */
    @JsonIgnore
    public Channel getChannel() {
        return channel;
    }
}
