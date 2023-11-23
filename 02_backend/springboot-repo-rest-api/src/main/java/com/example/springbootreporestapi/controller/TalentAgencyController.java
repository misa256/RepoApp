package com.example.springbootreporestapi.controller;

import com.example.springbootreporestapi.payload.TalentAgencyDto;
import com.example.springbootreporestapi.payload.TalentAgencyResponse;
import com.example.springbootreporestapi.service.TalentAgencyService;
import com.example.springbootreporestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repo/agency")
public class TalentAgencyController {

    @Autowired
    private TalentAgencyService talentAgencyService;

    @GetMapping
    public ResponseEntity<TalentAgencyResponse> getAll(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ){
        return ResponseEntity.ok(talentAgencyService.getAll(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TalentAgencyDto> getTalentAgency(
            @PathVariable(name = "id") Long id
    ){
        return ResponseEntity.ok(talentAgencyService.getTalentAgency(id));
    }

    @PostMapping
    public ResponseEntity<TalentAgencyDto> createTalentAgency(
            @RequestBody TalentAgencyDto talentAgencyDto
    ){
        return new ResponseEntity<>(talentAgencyService.createTalentAgency(talentAgencyDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TalentAgencyDto> updateTalentAgency(
            @PathVariable(name = "id") Long id,
            @RequestBody TalentAgencyDto talentAgencyDto
    ){
        return ResponseEntity.ok(talentAgencyService.updateTalentAgency(id, talentAgencyDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTalentAgency(
            @PathVariable(name = "id") Long id
    ){
        return ResponseEntity.ok(talentAgencyService.deleteTalentAgency(id));
    }

    @GetMapping("/search")
    public ResponseEntity<TalentAgencyResponse> searchTalentAgency(
            @RequestParam("agencyName") String agencyName,
            @RequestParam("country") String country,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ){
        return ResponseEntity.ok(talentAgencyService.searchTalentAgency(agencyName, country, pageNo, pageSize, sortBy, sortDir));
    }
}
