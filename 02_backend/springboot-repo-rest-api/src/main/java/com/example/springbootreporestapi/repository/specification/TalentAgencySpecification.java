package com.example.springbootreporestapi.repository.specification;

import com.example.springbootreporestapi.entity.TalentAgency;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TalentAgencySpecification {

    public Specification<TalentAgency>findByAgencyName(String agencyName){
        return StringUtils.isEmpty(agencyName) ? null : ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("agencyName"), agencyName)
                );
    }

    public Specification<TalentAgency>findByCountry(String country){
        return StringUtils.isEmpty(country) ? null : ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("country"), country)
        );
    }
}
