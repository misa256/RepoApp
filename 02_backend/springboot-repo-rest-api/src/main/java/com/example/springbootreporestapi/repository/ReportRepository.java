package com.example.springbootreporestapi.repository;

import com.example.springbootreporestapi.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report,Long>, JpaSpecificationExecutor<Report> {

    List<Report> findByArtistId(Long artistId, Sort sort);
}
