package com.foryoung.foryoung.search.service;

import com.foryoung.foryoung.search.dto.PerformanceSearchResponse;
import com.foryoung.foryoung.search.mapper.PerformanceSearchMapper;
import com.foryoung.foryoung.search.repository.PerformanceSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceSearchService {


    private final PerformanceSearchRepository searchRepository;
    private final PerformanceSearchMapper searchMapper;


    public List<PerformanceSearchResponse> searchPerformances(String keyword) {

        return searchRepository
                .search(keyword)
                .stream()
                .map(searchMapper::toPerformanceSearchResponse)
                .toList();

    }


}