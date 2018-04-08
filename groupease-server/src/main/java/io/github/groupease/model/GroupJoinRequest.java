package io.github.groupease.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "GroupJoinRequest")
public class GroupJoinRequest {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GroupID", referencedColumnName = "ID")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "SenderID", referencedColumnName = "ID")
    private Member sender;

    private String comments;

    @UpdateTimestamp
    private Instant lastUpdate;

    /**
     * Gets the unique ID of this join request
     * @return The unique ID of the join request
     */
    public Long getId() {
        return id;
    }

    /**
     * Get the {@link Group} that the sender wants to join
     * @return The group that the sender wants to join
     */
    public Group getGroup() {
        return group;
    }

    public Member getSender() {
        return sender;
    }

    /**
     * Gets the last time the persisted version of this join request was modified
     * @return The last time the persisted version of this join request was modified
     */
    public Instant getLastUpdate() {
        return lastUpdate;
    }
}
