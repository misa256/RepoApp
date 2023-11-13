package com.example.springbootreporestapi.service;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.payload.ArtistDto;
import com.example.springbootreporestapi.payload.ArtistResponse;
import com.example.springbootreporestapi.payload.InputArtistDto;

import java.util.List;

public interface ArtistsService {
    //全件取得
    ArtistResponse getAllArtists(int pageNo, int pageSize, String sortBy, String sortDir);
    //ArtistクラスのIDで取得
    ArtistDto getArtist(Long id);
    //Artistを新規登録
    ArtistDto createArtist(InputArtistDto inputArtistDto);
    //Artistを更新
    ArtistDto updateArtist(InputArtistDto inputArtistDto, Long id);
    //Artistを削除
    String deleteArtist(Long id);
    //Artistを検索
    ArtistResponse searchArtist(String name, String agencyName, String country, int pageNo, int pageSize, String sortBy, String sortDir);

}
