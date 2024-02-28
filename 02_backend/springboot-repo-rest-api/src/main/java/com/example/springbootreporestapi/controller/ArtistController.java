package com.example.springbootreporestapi.controller;

import com.example.springbootreporestapi.payload.ArtistDto;
import com.example.springbootreporestapi.payload.InputArtistDto;
import com.example.springbootreporestapi.service.ArtistsService;
import com.example.springbootreporestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repo/artist")
public class ArtistController {
    @Autowired
    private ArtistsService artistsService;

    @GetMapping
    public ResponseEntity<List<ArtistDto>> getAllArtist(
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ){
        return ResponseEntity.ok(artistsService.getAllArtists(sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtist(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(artistsService.getArtist(id));
    }

    @PostMapping
    public ResponseEntity<ArtistDto> createArtist(@RequestBody InputArtistDto inputArtistDto){
        return new ResponseEntity<>(artistsService.createArtist(inputArtistDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDto> updateArtist(@RequestBody InputArtistDto inputArtistDto,
                                                  @PathVariable(name = "id") Long id){
        return new ResponseEntity<>(artistsService.updateArtist(inputArtistDto,id),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArtist(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(artistsService.deleteArtist(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArtistDto>> searchArtist(
            @RequestParam("name") String name,
            @RequestParam("agencyName") String agencyName,
            @RequestParam("country") String country,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir){
        return ResponseEntity.ok(artistsService.searchArtist(name, agencyName, country, sortBy, sortDir));
    }

    //    特定のアーティストのレポートの場所を全件表示
    @GetMapping("/{id}/reports/places")
    public ResponseEntity<List<String>> showAllPlaces(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(artistsService.showAllReportPlaces(id));
    }



}
