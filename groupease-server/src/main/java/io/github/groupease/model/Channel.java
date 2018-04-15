package io.github.groupease.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name="Channel")
public class Channel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String description;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    @OneToMany(mappedBy = "channel")
    private List<Member> members;

    public Long getId() {
        return id;
    }

    //public void setId(Long id) {
    //    this.id = id;
    //}

    public String getName() {
        return name;
    }

    //public void setName(String name) {
    //    this.name = name;
    //}

    public String getDescription() {
        return description;
    }

    //public void setDescription(String description) {
    //    this.description = description;
    //}

    @JsonIgnore
    public List<Member> getMembers() { return members; }

    public Instant getLastUpdatedOn() {
        return lastUpdatedOn;
    }
}
