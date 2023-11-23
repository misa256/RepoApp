package com.example.springbootreporestapi.repository;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.entity.TalentAgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TalentAgencyRepository extends JpaRepository<TalentAgency,Long> , JpaSpecificationExecutor<TalentAgency> {
    Optional<TalentAgency> findByAgencyNameIgnoreCase(String agencyName);
}
