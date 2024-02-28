package com.example.springbootreporestapi.service;

import com.example.springbootreporestapi.payload.TalentAgencyDto;

import java.util.List;

public interface TalentAgencyService {
//    全件取得
    List<TalentAgencyDto> getAll(String sortBy, String sortDir);
//    Idで指定された事務所を取得
    TalentAgencyDto getTalentAgency(Long id);
//    新規登録
    TalentAgencyDto createTalentAgency(TalentAgencyDto talentAgencyDto);
//    更新
    TalentAgencyDto updateTalentAgency(Long id, TalentAgencyDto talentAgencyDto);
//    削除
    String deleteTalentAgency(Long id);
//    検索
    List<TalentAgencyDto> searchTalentAgency(String agencyName, String country, String sortBy, String sortDir);
//    事務所名を返す
    List<String> getAllAgencyName();
//    国名を返す
    List<String> getAllCountry();

}
