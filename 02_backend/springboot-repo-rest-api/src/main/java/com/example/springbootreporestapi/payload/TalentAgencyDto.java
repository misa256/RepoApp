package com.example.springbootreporestapi.payload;

import lombok.Data;

@Data
public class TalentAgencyDto {
    private Long id;
    private String agencyName;
    private String country;
}
