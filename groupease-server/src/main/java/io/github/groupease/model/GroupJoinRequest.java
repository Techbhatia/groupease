package io.github.groupease.model;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
=======
>>>>>>> changes
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "GroupJoinRequest")
public class GroupJoinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GroupID", referencedColumnName = "ID")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "SenderID", referencedColumnName = "ID")
    private GroupeaseUser sender;


    private String comments;

    @UpdateTimestamp
    private Instant lastUpdate;


    public GroupJoinRequest() {}

    public GroupJoinRequest(GroupeaseUser sender, Group group, String comments)
    {
        this.sender = sender;
        this.group = group;
        this.comments=comments;
    }

    @Override
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }



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

    /**
     * Gets the {@link GroupeaseUser} that sent this join request
     * @return The user that sent the join request
     */
    public GroupeaseUser getSender() { return sender; }

    /**
     * Gets the free text comments the sender submitted with the request
     * @return The free text comments
     */
    public String getComments() { return comments; }

    /**
     * Gets the last time the persisted version of this join request was modified
     * @return The last time the persisted version of this join request was modified
     */
    public Instant getLastUpdate() {
        return lastUpdate;
    }
}
