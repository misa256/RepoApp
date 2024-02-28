package com.example.springbootreporestapi.service.impl;

import com.example.springbootreporestapi.entity.TalentAgency;
import com.example.springbootreporestapi.exception.RepoAPIException;
import com.example.springbootreporestapi.payload.TalentAgencyDto;
import com.example.springbootreporestapi.repository.TalentAgencyRepository;
import com.example.springbootreporestapi.repository.specification.TalentAgencySpecification;
import com.example.springbootreporestapi.service.TalentAgencyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TalentAgencyServiceImpl implements TalentAgencyService {

    @Autowired
    private TalentAgencyRepository talentAgencyRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TalentAgencySpecification talentAgencySpecification;

    @Override
    public List<TalentAgencyDto> getAll(String sortBy, String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        List<TalentAgency> talentAgencies = talentAgencyRepository.findAll(sort);
        return getTalentAgencyDtos(talentAgencies);
    }

    @Override
    public TalentAgencyDto getTalentAgency(Long id) {
        TalentAgency talentAgency = talentAgencyRepository.findById(id).orElseThrow(()->{
            return new RepoAPIException(HttpStatus.NOT_FOUND, "タレント所属事務所がありません。");
        });
        return mapToDto(talentAgency);
    }

    @Override
    public TalentAgencyDto createTalentAgency(TalentAgencyDto talentAgencyDto) {
        TalentAgency talentAgency = talentAgencyRepository.save(mapToEntity(talentAgencyDto));
        return mapToDto(talentAgency);
    }

    @Override
    public TalentAgencyDto updateTalentAgency(Long id, TalentAgencyDto talentAgencyDto) {
        TalentAgency talentAgency = talentAgencyRepository.findById(id).orElseThrow(()->{
            return new RepoAPIException(HttpStatus.NOT_FOUND, "タレント所属事務所がありません。");
        });
        talentAgency.setAgencyName(talentAgencyDto.getAgencyName());
        talentAgency.setCountry(talentAgencyDto.getCountry());
        TalentAgency updatedTalentAgency = talentAgencyRepository.save(talentAgency);
        return mapToDto(updatedTalentAgency);
    }

    @Override
    public String deleteTalentAgency(Long id) {
        TalentAgency talentAgency = talentAgencyRepository.findById(id).orElseThrow(()->{
            return new RepoAPIException(HttpStatus.NOT_FOUND, "タレント所属事務所がありません。");
        });
        talentAgencyRepository.delete(talentAgency);
        return "タレント事務所は削除されました";
    }

    @Override
    public List<TalentAgencyDto> searchTalentAgency(String agencyName, String country, String sortBy, String sortDir) {
        List<TalentAgency> talentAgencies = talentAgencyRepository.findAll(Specification
                .where(talentAgencySpecification.findByAgencyName(agencyName))
                .and(talentAgencySpecification.findByCountry(country))
        ,getSort(sortBy, sortDir));
        return getTalentAgencyDtos(talentAgencies);
    }

    @Override
    public List<String> getAllAgencyName() {
        List<String> agencyNames = talentAgencyRepository.getAllAgencyName();
        Collections.sort(agencyNames);
        return agencyNames;
    }

    @Override
    public List<String> getAllCountry() {
        List<String> countries = talentAgencyRepository.getAllCountry();
        Collections.sort(countries);
        return countries;
    }

    private TalentAgency mapToEntity(TalentAgencyDto talentAgencyDto){
        return modelMapper.map(talentAgencyDto, TalentAgency.class);
    }
    
    private TalentAgencyDto mapToDto(TalentAgency talentAgency){
        return modelMapper.map(talentAgency, TalentAgencyDto.class);
    }

    private Sort getSort(String sortBy, String sortDir) {
        //sortDirがASCだったら、昇順で、そうでなかったら降順でSortクラスを返す
        Sort.Order ascOrder = Sort.Order.asc(sortBy).ignoreCase();
        Sort.Order desOrder = Sort.Order.desc(sortBy).ignoreCase();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(ascOrder)
                : Sort.by(desOrder);
        return sort;
    }

    private List<TalentAgencyDto> getTalentAgencyDtos(List<TalentAgency> talentAgencies) {
        List<TalentAgencyDto> talentAgencyDtos = talentAgencies.stream().map(
                talentAgency -> mapToDto(talentAgency)
        ).collect(Collectors.toList());
        return talentAgencyDtos;
    }
    
    
}
