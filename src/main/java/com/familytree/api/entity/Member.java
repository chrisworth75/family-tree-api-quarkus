package com.familytree.api.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "members")
public class Member extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tree_id", nullable = false)
    public FamilyTree familyTree;

    @Column(name = "first_name", nullable = false)
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @Column(name = "birth_date")
    public LocalDate birthDate;

    @Column(name = "death_date")
    public LocalDate deathDate;

    @Enumerated(EnumType.STRING)
    public Gender gender;

    @Column(name = "is_alive")
    public Boolean isAlive;

    @OneToMany(mappedBy = "member1", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Relationship> relationshipsAsMember1 = new HashSet<>();

    @OneToMany(mappedBy = "member2", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Relationship> relationshipsAsMember2 = new HashSet<>();

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
