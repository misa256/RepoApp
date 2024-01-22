package com.example.springbootreporestapi.repository;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ArtistRepository extends JpaRepository<Artist, Long>,JpaSpecificationExecutor<Artist> {

    Optional<Artist> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a.reports FROM Artist a WHERE a.id = :id")
    Set<Report> showAllReportPlaces(Long id);

}
