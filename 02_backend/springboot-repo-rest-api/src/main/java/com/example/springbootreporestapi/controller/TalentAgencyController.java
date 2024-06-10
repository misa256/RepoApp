package com.example.springbootreporestapi.controller;

import com.example.springbootreporestapi.payload.TalentAgencyDto;
import com.example.springbootreporestapi.service.TalentAgencyService;
import com.example.springbootreporestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repoApi/agency")
public class TalentAgencyController {

    @Autowired
    private TalentAgencyService talentAgencyService;

    @GetMapping
    public ResponseEntity<List<TalentAgencyDto>> getAll(
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ){
        return ResponseEntity.ok(talentAgencyService.getAll(sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TalentAgencyDto> getTalentAgency(
            @PathVariable(name = "id") Long id
    ){
        return ResponseEntity.ok(talentAgencyService.getTalentAgency(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<TalentAgencyDto> createTalentAgency(
            @RequestBody TalentAgencyDto talentAgencyDto
    ){
        return new ResponseEntity<>(talentAgencyService.createTalentAgency(talentAgencyDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TalentAgencyDto> updateTalentAgency(
            @PathVariable(name = "id") Long id,
            @RequestBody TalentAgencyDto talentAgencyDto
    ){
        return ResponseEntity.ok(talentAgencyService.updateTalentAgency(id, talentAgencyDto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTalentAgency(
            @PathVariable(name = "id") Long id
    ){
        return ResponseEntity.ok(talentAgencyService.deleteTalentAgency(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TalentAgencyDto>> searchTalentAgency(
            @RequestParam("agencyName") String agencyName,
            @RequestParam("country") String country,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ){
        return ResponseEntity.ok(talentAgencyService.searchTalentAgency(agencyName, country, sortBy, sortDir));
    }

    @GetMapping("/getAgencyNames")
    public ResponseEntity<List<String>> getAllAgencyNames(){
        return ResponseEntity.ok(talentAgencyService.getAllAgencyName());
    }

    @GetMapping("/getAllCountries")
    public ResponseEntity<List<String>> getAllCountries(){
        return ResponseEntity.ok(talentAgencyService.getAllCountry());
    }
}
