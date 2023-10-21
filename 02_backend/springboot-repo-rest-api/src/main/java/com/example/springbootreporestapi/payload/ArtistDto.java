package com.example.springbootreporestapi.payload;

import com.example.springbootreporestapi.entity.Report;
import com.example.springbootreporestapi.entity.TalentAgency;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ArtistDto {
    private long id;
    private String name;
   private TalentAgency talentAgency;
    private Set<Report> reports;
}
