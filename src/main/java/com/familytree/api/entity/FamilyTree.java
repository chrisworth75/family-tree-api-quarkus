package com.familytree.api.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "family_trees")
public class FamilyTree extends PanacheEntity {

    @Column(nullable = false)
    public String name;

    @Column(name = "created_by")
    public String createdBy;

    @OneToMany(mappedBy = "familyTree", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Member> members = new HashSet<>();
}
