package com.example.springbootreporestapi.service;

import com.example.springbootreporestapi.payload.TalentAgencyDto;
import com.example.springbootreporestapi.payload.TalentAgencyResponse;

public interface TalentAgencyService {
//    全件取得
    TalentAgencyResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir);
//    Idで指定された事務所を取得
    TalentAgencyDto getTalentAgency(Long id);
//    新規登録
    TalentAgencyDto createTalentAgency(TalentAgencyDto talentAgencyDto);
//    更新
    TalentAgencyDto updateTalentAgency(Long id, TalentAgencyDto talentAgencyDto);
//    削除
    String deleteTalentAgency(Long id);
//    検索
    TalentAgencyResponse searchTalentAgency(String agencyName, String country, int pageNo, int pageSize, String sortBy, String sortDir);

}
