package com.medicow.service;

import com.medicow.model.dto.HosptialSearchResultDto;
import com.medicow.repository.inter.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor  // 생성자 주입
public class SearchService {

    private final SearchRepository searchRepository ;

    public List<HosptialSearchResultDto> findByHospital(String searchQuery , double user_x , double user_y){
        List<HosptialSearchResultDto> searchlists = searchRepository.findByHospital(searchQuery, user_x, user_y) ;

        return searchlists ;

    }


}
