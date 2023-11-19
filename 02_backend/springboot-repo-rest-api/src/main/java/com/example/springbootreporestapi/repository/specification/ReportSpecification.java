package com.example.springbootreporestapi.repository.specification;

import com.example.springbootreporestapi.entity.Artist;
import com.example.springbootreporestapi.entity.Report;
import com.example.springbootreporestapi.entity.TalentAgency;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ReportSpecification {
    //アーティストで絞り込み
    public Specification<Report> findByArtistId(Long artistId){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("artist").get("id"), artistId));
    }

    //場所で絞り込み
    public Specification<Report> findByPlace(String place){
        return StringUtils.isEmpty(place) ? null : ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("place"), place)
        );
    }

    //日付で絞り込み
    public Specification<Report> findByDate(String date){
        return StringUtils.isEmpty(date) ? null : ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("date"), date)
        );
    }

    //タイトルに含まれる文字で絞り込み
    public Specification<Report> findByContainingTitle(String title){
        return StringUtils.isEmpty(title) ? null : (((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"),"%"+title+"%")
                ));
    }
}
