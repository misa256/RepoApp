package com.example.springbootreporestapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "talentAgencies", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class TalentAgency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "country")
    private String country;
    @OneToMany(mappedBy = "talentAgency", orphanRemoval = true)
    private List<Artist> artists = new ArrayList<>();
}
