package com.familytree.api.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "relationships")
public class Relationship extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member1_id", nullable = false)
    public Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member2_id", nullable = false)
    public Member member2;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_type", nullable = false)
    public RelationshipType relationshipType;

    public enum RelationshipType {
        PARENT, CHILD, PARTNER, SIBLING
    }
}
