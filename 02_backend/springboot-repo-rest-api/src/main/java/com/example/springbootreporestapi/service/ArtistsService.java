package com.example.springbootreporestapi.service;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.entity.Report;
import com.example.springbootreporestapi.payload.ArtistDto;
import com.example.springbootreporestapi.payload.InputArtistDto;

import java.util.List;
import java.util.Set;

public interface ArtistsService {
    //全件取得
    List<ArtistDto> getAllArtists(String sortBy, String sortDir);
    //ArtistクラスのIDで取得
    ArtistDto getArtist(Long id);
    //Artistを新規登録
    ArtistDto createArtist(InputArtistDto inputArtistDto);
    //Artistを更新
    ArtistDto updateArtist(InputArtistDto inputArtistDto, Long id);
    //Artistを削除
    String deleteArtist(Long id);
    //Artistを検索
    List<ArtistDto> searchArtist(String name, String agencyName, String country, String sortBy, String sortDir);
    //    特定のアーティストのレポートの場所を全件表示
    List<String> showAllReportPlaces(Long id);
}
