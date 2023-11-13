package com.example.springbootreporestapi.repository.specification;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.entity.TalentAgency;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ArtistSpecifications {

//    public Specification<Artist> join(){
//        return (root,query,builder) -> {
//        root.fetch("talentAgency", JoinType.LEFT);
//        return null;
//        };
//    }

    public Specification<Artist> findByNameContainingIgnoreCase(String name){
        return StringUtils.isEmpty(name) ? null : (root, query, builder) ->
            builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };

    public Specification<Artist> findByAgencyName(String agencyName){
        return StringUtils.isEmpty(agencyName) ? null : ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<TalentAgency>get("talentAgency").get("agencyName"), agencyName)
                );
    }

    public Specification<Artist> findByCountry(String country){
        return StringUtils.isEmpty(country) ? null : ((root, query, builder)->
                builder.equal(root.get("talentAgency").get("country"), country)
        );

    }

    }
