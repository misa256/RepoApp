package com.example.springbootreporestapi.service.impl;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.entity.Report;
import com.example.springbootreporestapi.entity.TalentAgency;
import com.example.springbootreporestapi.exception.RepoAPIException;
import com.example.springbootreporestapi.exception.ResourceNotFoundException;
import com.example.springbootreporestapi.payload.ArtistDto;
import com.example.springbootreporestapi.payload.InputArtistDto;
import com.example.springbootreporestapi.repository.ArtistRepository;
import com.example.springbootreporestapi.repository.TalentAgencyRepository;
import com.example.springbootreporestapi.repository.specification.ArtistSpecifications;
import com.example.springbootreporestapi.service.ArtistsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArtistsServiceImpl implements ArtistsService {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TalentAgencyRepository talentAgencyRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ArtistSpecifications artistSpecifications;

    @Override
    public List<ArtistDto> getAllArtists(String sortBy, String sortDir) {

        //sortDirがASCだったら、昇順で、そうでなかったら降順でSortクラスを返す
        Sort.Order ascOrder = Sort.Order.asc(sortBy).ignoreCase();
        Sort.Order desOrder = Sort.Order.desc(sortBy).ignoreCase();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(ascOrder)
                : Sort.by(desOrder);
        //ページングに関する情報と、中身を持つ
        List<Artist> artists = artistRepository.findAll(sort);

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
    public ArtistDto createArtist(InputArtistDto inputArtistDto) {
        //アーティストがすでに登録されていないか
        if(artistRepository.findByNameContainingIgnoreCase(inputArtistDto.getName()).isPresent()){
        throw new RepoAPIException(HttpStatus.BAD_REQUEST,"既にそのアーティストは登録されています。");
        }
        //アーティスト新規登録
        Artist artist = new Artist();
        TalentAgency talentAgency = talentAgencyRepository.findById(inputArtistDto.getTalentAgencyId())
                .orElseThrow(()->{
                    throw new RepoAPIException(HttpStatus.NOT_FOUND, "タレント事務所が存在しません。管理者にタレント事務所登録を依頼してください。");
                });
        artist.setName(inputArtistDto.getName());
        artist.setTalentAgency(talentAgency);
        Artist responseEntity = artistRepository.save(artist);
        return mapToDto(responseEntity);
    }

    @Override
    public ArtistDto updateArtist(InputArtistDto inputArtistDto, Long id) {
        //更新対象のアーティストが存在するか
        Artist artist = artistRepository.findById(id).orElseThrow(()->{
            throw new ResourceNotFoundException("アーティスト","id",id);
        });
        TalentAgency talentAgency = talentAgencyRepository.findById(inputArtistDto.getTalentAgencyId())
                .orElseThrow(()->{
                    throw new RepoAPIException(HttpStatus.NOT_FOUND, "タレント事務所が存在しません。管理者にタレント事務所登録を依頼してください。");
                });
        //更新
        artist.setName(inputArtistDto.getName());
        artist.setTalentAgency(talentAgency);
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
    public List<ArtistDto> searchArtist(String name, String agencyName, String country, String sortBy, String sortDir){
        Sort.Order ascOrder = Sort.Order.asc(sortBy).ignoreCase();
        Sort.Order desOrder = Sort.Order.desc(sortBy).ignoreCase();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ascOrder)
                : Sort.by(desOrder);
        List<Artist> artists = artistRepository.findAll(Specification
                .where(artistSpecifications.findByNameContainingIgnoreCase(name))
                .and(artistSpecifications.findByAgencyName(agencyName))
                .and(artistSpecifications.findByCountry(country)), sort);
        if (artists.size() == 0){
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "アーティストが存在しません。条件を変更してください。");
        }
        List<ArtistDto> artistDtos = artists.stream()
                .map(artist-> mapToDto(artist))
                .collect(Collectors.toList());
        return artistDtos;
    }

    @Override
    public List<String> showAllReportPlaces(Long id) {
        Set<Report> reports = artistRepository.showAllReportPlaces(id);
        List<String> resultList = new ArrayList<>();
        if(reports.isEmpty()){
             resultList.add("レポートが投稿されていません");
             return resultList;
        };
        reports.stream().map(report ->
           resultList.add(report.getPlace())
        ).collect(Collectors.toList());
        List<String> distinctResultList = new ArrayList<>(new TreeSet<>(resultList));
        return distinctResultList;
    }

    private ArtistDto mapToDto(Artist artist){
        return mapper.map(artist,ArtistDto.class);
    }

    private Artist mapToEntity(ArtistDto artistDto){return mapper.map(artistDto,Artist.class);}
}
