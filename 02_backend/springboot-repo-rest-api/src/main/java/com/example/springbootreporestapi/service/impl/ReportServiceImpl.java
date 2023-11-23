package com.example.springbootreporestapi.service.impl;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.entity.Report;
import com.example.springbootreporestapi.exception.RepoAPIException;
import com.example.springbootreporestapi.exception.ResourceNotFoundException;
import com.example.springbootreporestapi.payload.ArtistDto;
import com.example.springbootreporestapi.payload.ArtistResponse;
import com.example.springbootreporestapi.payload.ReportDto;
import com.example.springbootreporestapi.payload.ReportResponse;
import com.example.springbootreporestapi.repository.ArtistRepository;
import com.example.springbootreporestapi.repository.ReportRepository;
import com.example.springbootreporestapi.repository.specification.ReportSpecification;
import com.example.springbootreporestapi.service.ReportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ReportSpecification reportSpecification;

    @Override
    public ReportResponse getAllReport(Long artistId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);

        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> {
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "アーティストが存在しません。管理者にアーティストの登録を依頼してください。");
        });
        Page<Report> pageOfReports = reportRepository.findByArtistId(artistId, pageable).orElseThrow(() -> {
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "アーティストのレポートが存在しません。レポートの登録は以下から行えます。");
        });
        List<ReportDto> reportDtos = pageOfReports.getContent()
                .stream()
                .map(report -> mapToDto(report))
                .collect(Collectors.toList());
        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setContent(reportDtos);
        reportResponse.setPageNo(pageOfReports.getNumber());
        reportResponse.setPageSize(pageOfReports.getSize());
        reportResponse.setTotalPages(pageOfReports.getTotalPages());
        reportResponse.setTotalElements(pageOfReports.getTotalElements());
        reportResponse.setLast(pageOfReports.isLast());
        return reportResponse;
    }

    @Override
    public ReportDto createReport(Long artistId, ReportDto reportDto) {
        Report report = mapToEntity(reportDto);
        Artist artist = artistRepository.findById(artistId).orElseThrow(
                () -> {
                    throw new RepoAPIException(HttpStatus.NOT_FOUND, "アーティストが存在しません。管理者にアーティストの登録を依頼してください。");
                }
        );
        report.setArtist(artist);
        reportRepository.save(report);
        return mapToDto(report);
    }

    @Override
    public ReportDto getReport(Long artistId, Long id) {
        Report report = checkArtistAndReport(artistId, id);
        return mapToDto(report);
    }


    @Override
    public ReportDto updateReport(Long artistId, Long id, ReportDto reportDto) {
        Report report = checkArtistAndReport(artistId, id);
        report.setPlace(reportDto.getPlace());
        report.setDate(reportDto.getDate());
        report.setTitle(reportDto.getTitle());
        report.setText(reportDto.getText());
        Report updatedReport = reportRepository.save(report);
        return mapToDto(updatedReport);
    }

    @Override
    public String deleteReport(Long artistId, Long id) {
        Report report = checkArtistAndReport(artistId, id);
        reportRepository.delete(report);
        return "レポートを削除しました。";
    }

    @Override
    public ReportResponse searchReport(Long artistId, int pageNo, int pageSize, String sortBy, String sortDir, String place, String date, String title) {
        Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);
        Page<Report> pageOfReport = reportRepository.findAll(Specification.where(
                                reportSpecification.findByArtistId(artistId))
                        .and(reportSpecification.findByPlace(place))
                        .and(reportSpecification.findByDate(date))
                        .and(reportSpecification.findByContainingTitle(title))
                , pageable);
        List<Report> listOfReport = pageOfReport.getContent();
        if (listOfReport.size() == 0) {
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "レポートが存在しません。");
        }
        List<ReportDto> content = listOfReport.stream()
                .map(report -> mapToDto(report))
                .collect(Collectors.toList());
        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setContent(content);
        reportResponse.setPageNo(pageOfReport.getNumber());
        reportResponse.setPageSize(pageOfReport.getSize());
        reportResponse.setTotalElements(pageOfReport.getTotalElements());
        reportResponse.setTotalPages(pageOfReport.getTotalPages());
        reportResponse.setLast(pageOfReport.isLast());
        return reportResponse;
    }

    private ReportDto mapToDto(Report report) {
        return mapper.map(report, ReportDto.class);
    }

    private Report mapToEntity(ReportDto reportDto) {
        return mapper.map(reportDto, Report.class);
    }

    private Pageable getPageable(int pageNo, int pageSize, String sortBy, String sortDir) {
        //sortDirがASCだったら、昇順で、そうでなかったら降順でSortクラスを返す
        Sort.Order ascOrder = Sort.Order.asc(sortBy).ignoreCase();
        Sort.Order desOrder = Sort.Order.desc(sortBy).ignoreCase();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(ascOrder)
                : Sort.by(desOrder);

        //ページングに関する情報を持つパラメータを作成
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return pageable;
    }

    private Report checkArtistAndReport(Long artistId, Long id) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> {
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "アーティストが存在しません。管理者にアーティストの登録を依頼してください。");
        });

        Report report = reportRepository.findById(id).orElseThrow(() -> {
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "アーティストのレポートが存在しません。レポートの登録は以下から行えます。");
        });

        if (!(report.acquireArtist().getId() == artist.getId())) {
            throw new RepoAPIException(HttpStatus.BAD_REQUEST, "このアーティストのレポートではありません。");
        }
        return report;
    }
}
