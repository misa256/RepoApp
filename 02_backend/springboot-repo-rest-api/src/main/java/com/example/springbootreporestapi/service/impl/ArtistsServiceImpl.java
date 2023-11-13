package com.example.springbootreporestapi.service.impl;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.entity.TalentAgency;
import com.example.springbootreporestapi.exception.RepoAPIException;
import com.example.springbootreporestapi.exception.ResourceNotFoundException;
import com.example.springbootreporestapi.payload.ArtistDto;
import com.example.springbootreporestapi.payload.ArtistResponse;
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

    @Autowired
    private ArtistSpecifications artistSpecifications;

    @Override
    public ArtistResponse getAllArtists(int pageNo, int pageSize, String sortBy, String sortDir) {

        //sortDirがASCだったら、昇順で、そうでなかったら降順でSortクラスを返す
        Sort.Order ascOrder = Sort.Order.asc(sortBy).ignoreCase();
        Sort.Order desOrder = Sort.Order.desc(sortBy).ignoreCase();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(ascOrder)
                : Sort.by(desOrder);

        //ページングに関する情報を持つパラメータを作成
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        //ページングに関する情報と、中身を持つ
        Page<Artist> artists = artistRepository.findAll(pageable);
        //ページ情報から、中身を取ってくる
        List<Artist> listOfArtist = artists.getContent();
        
        List<ArtistDto> content = listOfArtist.stream()
                .map(artist -> mapToDto(artist))
                .collect(Collectors.toList());

        ArtistResponse artistResponse = new ArtistResponse();
        artistResponse.setContent(content);
        artistResponse.setPageNo(artists.getNumber());
        artistResponse.setPageSize(artists.getSize());
        artistResponse.setTotalElements(artists.getTotalElements());
        artistResponse.setTotalPages(artists.getTotalPages());
        artistResponse.setLast(artists.isLast());

        return artistResponse;
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
        artist.setReports(inputArtistDto.getReports());
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
    public ArtistResponse searchArtist(String name, String agencyName, String country, int pageNo, int pageSize, String sortBy, String sortDir){
        Sort.Order ascOrder = Sort.Order.asc(sortBy).ignoreCase();
        Sort.Order desOrder = Sort.Order.desc(sortBy).ignoreCase();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ascOrder)
                : Sort.by(desOrder);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Artist> artists = artistRepository.findAll(Specification
                .where(artistSpecifications.findByNameContainingIgnoreCase(name))
                .and(artistSpecifications.findByAgencyName(agencyName))
                .and(artistSpecifications.findByCountry(country)), pageable);
        List<Artist> listOfArtist = artists.getContent();
        if (listOfArtist.size() == 0){
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "アーティストが存在しません。");
        }
        List<ArtistDto> content = listOfArtist.stream()
                .map(artist-> mapToDto(artist))
                .collect(Collectors.toList());
        ArtistResponse artistResponse = new ArtistResponse();
        artistResponse.setContent(content);
        artistResponse.setPageNo(artists.getNumber());
        artistResponse.setPageSize(artists.getSize());
        artistResponse.setTotalElements(artists.getTotalElements());
        artistResponse.setTotalPages(artists.getTotalPages());
        artistResponse.setLast(artists.isLast());
        return artistResponse;
    }

    private ArtistDto mapToDto(Artist artist){
        return mapper.map(artist,ArtistDto.class);
    }

    private Artist mapToEntity(ArtistDto artistDto){return mapper.map(artistDto,Artist.class);}
}
