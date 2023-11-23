package com.example.springbootreporestapi.service.impl;

import com.example.springbootreporestapi.entity.TalentAgency;
import com.example.springbootreporestapi.exception.RepoAPIException;
import com.example.springbootreporestapi.payload.TalentAgencyDto;
import com.example.springbootreporestapi.payload.TalentAgencyResponse;
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
    public TalentAgencyResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);
        Page pageOfTalentAgency = talentAgencyRepository.findAll(pageable);
        TalentAgencyResponse talentAgencyResponse = getTalentAgencyResponse(pageOfTalentAgency);
        return  talentAgencyResponse;
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
    public TalentAgencyResponse searchTalentAgency(String agencyName, String country, int pageNo, int pageSize, String sortBy, String sortDir) {
        Page<TalentAgency> pageOfTalentAgency = talentAgencyRepository.findAll(Specification
                .where(talentAgencySpecification.findByAgencyName(agencyName))
                .and(talentAgencySpecification.findByCountry(country))
        ,getPageable(pageNo, pageSize, sortBy, sortDir));
        TalentAgencyResponse talentAgencyResponse = getTalentAgencyResponse(pageOfTalentAgency);
        return talentAgencyResponse;
    }

    private TalentAgency mapToEntity(TalentAgencyDto talentAgencyDto){
        return modelMapper.map(talentAgencyDto, TalentAgency.class);
    }
    
    private TalentAgencyDto mapToDto(TalentAgency talentAgency){
        return modelMapper.map(talentAgency, TalentAgencyDto.class);
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

    private TalentAgencyResponse getTalentAgencyResponse(Page pageOfTalentAgency) {
        List<TalentAgency> listOfTalentAgency = pageOfTalentAgency.getContent();
        List<TalentAgencyDto> content = listOfTalentAgency.stream().map(
                talentAgency -> mapToDto(talentAgency)
        ).collect(Collectors.toList());
        TalentAgencyResponse talentAgencyResponse = new TalentAgencyResponse();
        talentAgencyResponse.setContent(content);
        talentAgencyResponse.setPageNo(pageOfTalentAgency.getNumber());
        talentAgencyResponse.setPageSize(pageOfTalentAgency.getSize());
        talentAgencyResponse.setTotalElements(pageOfTalentAgency.getTotalElements());
        talentAgencyResponse.setTotalPages(pageOfTalentAgency.getTotalPages());
        talentAgencyResponse.setLast(pageOfTalentAgency.isLast());
        return talentAgencyResponse;
    }
    
    
}
