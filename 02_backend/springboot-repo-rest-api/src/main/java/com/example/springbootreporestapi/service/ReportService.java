package com.example.springbootreporestapi.service;

import com.example.springbootreporestapi.payload.InputReportDto;
import com.example.springbootreporestapi.payload.ReportDto;

import java.util.List;

public interface ReportService {
//    特定のアーティストのレポート全件取得
    List<ReportDto> getAllReport(Long artistId, String sortBy, String sortDir);
//    特定のアーティストのレポートを新規作成
    ReportDto createReport(Long artistId, InputReportDto inputReportDto);

//    特定のアーティストの特定のレポートを取得
    ReportDto getReport(Long artistId, Long id);

//    特定のアーティストの特定のレポートを更新
    ReportDto updateReport(Long artistId, Long id, ReportDto reportDto);

//    特定のアーティストの特定のレポートを削除
    String deleteReport(Long artistId, Long id);

//    特定のアーティストのレポートを検索
    List<ReportDto> searchReport(Long artistId, String sortBy, String sortDir,
                                String place, String date, String title);

//    ユーザーが作成したレポート全件取得
    List<ReportDto> getUserAllReport(Long userId);
}
