package io.github.groupease.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
=======
>>>>>>> changes
import org.hibernate.annotations.UpdateTimestamp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

    @Override
    public boolean equals(
            @Nullable Object o
    ) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Nonnull
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
