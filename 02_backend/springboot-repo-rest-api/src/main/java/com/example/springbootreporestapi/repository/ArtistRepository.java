package com.example.springbootreporestapi.repository;

import com.example.springbootreporestapi.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long>,JpaSpecificationExecutor<Artist> {

    Optional<Artist> findByNameContainingIgnoreCase(String name);

}
