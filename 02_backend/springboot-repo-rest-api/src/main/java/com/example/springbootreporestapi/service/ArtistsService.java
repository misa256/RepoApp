package com.example.springbootreporestapi.service;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.payload.ArtistDto;

import java.util.List;

public interface ArtistsService {
    //全件取得
    List<ArtistDto> getAllArtists();
    //ArtistクラスのIDで取得
    ArtistDto getArtist(Long id);
    //Artistを新規登録
    ArtistDto createArtist(Long talentAgencyId, ArtistDto artistDto);
    //Artistを更新
    ArtistDto updateArtist(ArtistDto artistDto, Long id);
    //Artistを削除
    String deleteArtist(Long id);
    //Artistを名前で検索
    ArtistDto searchArtistByName(String name);

}
