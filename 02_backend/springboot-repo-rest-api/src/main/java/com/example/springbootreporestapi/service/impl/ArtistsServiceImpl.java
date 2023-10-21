package com.example.springbootreporestapi.service.impl;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.entity.TalentAgency;
import com.example.springbootreporestapi.exception.RepoAPIException;
import com.example.springbootreporestapi.exception.ResourceNotFoundException;
import com.example.springbootreporestapi.payload.ArtistDto;
import com.example.springbootreporestapi.repository.ArtistRepository;
import com.example.springbootreporestapi.repository.TalentAgencyRepository;
import com.example.springbootreporestapi.service.ArtistsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtistsServiceImpl implements ArtistsService {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TalentAgencyRepository talentAgencyRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<ArtistDto> getAllArtists() {
        List<Artist> artists = artistRepository.findAll();
        List<ArtistDto> artistDtos = artists.stream()
                .map(artist -> mapToDto(artist))
                .collect(Collectors.toList());
        return artistDtos;
    }

    @Override
    public ArtistDto getArtist(Long id) {
        Optional<Artist> artist = artistRepository.findById(id);
        if(!artist.isPresent()){
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "アーティストが存在しません。管理者にアーティスト登録を依頼してください。");
        }
        return  mapToDto(artist.get());
    }

    @Override
    public ArtistDto createArtist(Long talentAgencyId, ArtistDto artistDto) {
        //アーティストがすでに登録されていないか
        if(artistRepository.findByNameContainingIgnoreCase(artistDto.getName()).isPresent()){
        throw new RepoAPIException(HttpStatus.BAD_REQUEST,"既にそのアーティストは登録されています。");
        }
        //アーティスト新規登録
        Artist artist = mapToEntity(artistDto);
        TalentAgency talentAgency = talentAgencyRepository.findById(talentAgencyId)
                .orElseThrow(()->{
                    throw new RepoAPIException(HttpStatus.NOT_FOUND, "タレント事務所が存在しません。管理者にタレント事務所登録を依頼してください。");
                });
        artist.setTalentAgency(talentAgency);
        Artist responseEntity = artistRepository.save(artist);
        return mapToDto(responseEntity);
    }

    @Override
    public ArtistDto updateArtist(ArtistDto artistDto, Long id) {
        //更新対象のアーティストが存在するか
        Artist artist = artistRepository.findById(id).orElseThrow(()->{
            throw new ResourceNotFoundException("アーティスト","id",id);
        });
        //更新
        artist.setName(artistDto.getName());
        artist.setTalentAgency(artistDto.getTalentAgency());
        Artist updatedArtist = artistRepository.save(artist);
        //エンティティ→DTOに詰め替えてreturn
        return  mapToDto(updatedArtist);
    }

    @Override
    public String deleteArtist(Long id) {
        //削除対象のアーティストが存在するか
        Artist artist = artistRepository.findById(id).orElseThrow(()->{
            throw new ResourceNotFoundException("アーティスト","id",id);
        });
        artistRepository.deleteById(id);
        return "アーティストを削除しました。";
    }

    @Override
    public ArtistDto searchArtistByName(String name) {
        Artist artist = artistRepository.findByNameContainingIgnoreCase(name).orElseThrow(()->{
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "アーティストが存在しません。管理者にアーティスト登録を依頼してください。");
        });
        return mapToDto(artist);
    }

    private ArtistDto mapToDto(Artist artist){
        return mapper.map(artist, ArtistDto.class);
    }

    private Artist mapToEntity(ArtistDto artistDto){return mapper.map(artistDto,Artist.class);}
}
