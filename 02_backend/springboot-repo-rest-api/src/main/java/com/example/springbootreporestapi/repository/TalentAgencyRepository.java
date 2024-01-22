package com.example.springbootreporestapi.repository;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.entity.TalentAgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TalentAgencyRepository extends JpaRepository<TalentAgency,Long> , JpaSpecificationExecutor<TalentAgency> {
    Optional<TalentAgency> findByAgencyNameIgnoreCase(String agencyName);

    @Query(value = "SELECT agencyName FROM TalentAgency")
    List<String> getAllAgencyName();

    @Query(value = "SELECT DISTINCT country FROM TalentAgency")
    List<String> getAllCountry();
}
