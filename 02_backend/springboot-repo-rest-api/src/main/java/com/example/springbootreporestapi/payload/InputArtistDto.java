package com.example.springbootreporestapi.payload;

import com.example.springbootreporestapi.entity.Report;
import com.example.springbootreporestapi.entity.TalentAgency;
import lombok.Data;

import java.util.Set;

@Data
public class InputArtistDto {
    private String name;
    private long talentAgencyId;
}
