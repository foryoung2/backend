package com.foryoung.foryoung.search.service;

import com.foryoung.foryoung.search.dto.PerformanceSearchResponse;
import com.foryoung.foryoung.search.dto.PerformanceSearchResult;
import com.foryoung.foryoung.search.mapper.PerformanceSearchMapper;
import com.foryoung.foryoung.search.repository.PerformanceSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceSearchService {

    private final PerformanceSearchRepository searchRepository;
    private final PerformanceSearchMapper searchMapper;


    public Page<PerformanceSearchResponse> searchPerformances(String keyword,
                                                              Pageable pageable) {

        PerformanceSearchResult result = searchRepository.search(keyword, pageable.getPageNumber(), pageable.getPageSize());

        List<PerformanceSearchResponse> content =
                result.getDocuments()
                        .stream()
                        .map(searchMapper::toPerformanceSearchResponse)
                        .toList();

        return new PageImpl<>(
                content,
                pageable,
                result.getTotalElements()
        );

    }


}