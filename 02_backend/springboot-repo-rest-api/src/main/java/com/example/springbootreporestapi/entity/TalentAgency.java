package com.example.springbootreporestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "talentAgencies", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "agencyName"))
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class TalentAgency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "agencyName", nullable = false)
    private String agencyName;
    @Column(name = "country")
    private String country;
}
