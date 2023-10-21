package com.example.springbootreporestapi.controller;

import com.example.springbootreporestapi.payload.ArtistDto;
import com.example.springbootreporestapi.service.ArtistsService;
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
    public ResponseEntity<List<ArtistDto>> getAllArtist(){
        return ResponseEntity.ok(artistsService.getAllArtists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtist(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(artistsService.getArtist(id));
    }

    @PostMapping("/{talentAgencyId}")
    public ResponseEntity<ArtistDto> createArtist(@PathVariable(name = "talentAgencyId") Long talentAgencyId,
                                                  @RequestBody ArtistDto artistDto){
        return new ResponseEntity<>(artistsService.createArtist(talentAgencyId,artistDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDto> updateArtist(@RequestBody ArtistDto artistDto,
                                                  @PathVariable(name = "id") Long id){
        return new ResponseEntity<>(artistsService.updateArtist(artistDto,id),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArtist(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(artistsService.deleteArtist(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ArtistDto> searchArtistByName(@RequestParam("name") String name){
        return ResponseEntity.ok(artistsService.searchArtistByName(name));
    }




}
