package com.example.springbootreporestapi.controller;

import com.example.springbootreporestapi.payload.ReportDto;
import com.example.springbootreporestapi.payload.ReportResponse;
import com.example.springbootreporestapi.service.ReportService;
import com.example.springbootreporestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repo")
public class ReportController {

    @Autowired
    private ReportService reportService;

//    特定のアーティストのレポート全件取得
    @GetMapping("/artist/{artistId}/reports")
    public ResponseEntity<ReportResponse> getAllReports(
            @PathVariable(name = "artistId") Long artistId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ){
        return ResponseEntity.ok(reportService.getAllReport(artistId, pageNo, pageSize, sortBy, sortDir));
    }
//特定のアーティストのレポートを新規作成
    @PostMapping("/artist/{artistId}/reports")
    public ResponseEntity<ReportDto> createReport(
            @PathVariable(name = "artistId") Long artistId,
            @RequestBody ReportDto reportDto
    ){
    return new ResponseEntity<>(reportService.createReport(artistId, reportDto), HttpStatus.CREATED);
    }

//    特定のアーティストの特定のレポートを取得
    @GetMapping("/artist/{artistId}/reports/{id}")
    public ResponseEntity<ReportDto> getReport(
            @PathVariable(name = "artistId") Long artistId,
            @PathVariable(name = "id") Long id
    ){
    return ResponseEntity.ok(reportService.getReport(artistId, id));
    }

//    特定のアーティストの特定のレポートを更新
    @PutMapping("/artist/{artistId}/reports/{id}")
    public ResponseEntity<ReportDto> updateReport(
            @PathVariable(name = "artistId") Long artistId,
            @PathVariable(name = "id") Long id,
            @RequestBody ReportDto reportDto
    ){
        return new ResponseEntity<>(reportService.updateReport(artistId, id, reportDto), HttpStatus.OK);
    }

//    特定のアーティストの特定のレポートを削除
    @DeleteMapping("/artist/{artistId}/reports/{id}")
    public ResponseEntity<String> deleteReport(
            @PathVariable(name = "artistId") Long artistId,
            @PathVariable(name = "id") Long id
    ){
        return new ResponseEntity<>(reportService.deleteReport(artistId, id), HttpStatus.OK);
    }

//    特定のアーティストのレポートを検索
    @GetMapping("/artist/{artistId}/reports/search")
    public ResponseEntity<ReportResponse> searchReport(
            @PathVariable(name = "artistId") Long artistId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "place") String place,
            @RequestParam(value = "date") String date,
            @RequestParam(value = "title") String title
    ){
    return ResponseEntity.ok(reportService.searchReport(artistId, pageNo, pageSize, sortBy, sortDir, place, date, title));
    }
}
