package com.foryoung.foryoung.search.service;

import com.foryoung.foryoung.search.dto.VenueSearchResponse;
import com.foryoung.foryoung.search.dto.VenueSearchResult;
import com.foryoung.foryoung.search.mapper.VenueSearchMapper;
import com.foryoung.foryoung.search.repository.VenueSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueSearchService {


    private final VenueSearchRepository searchRepository;
    private final VenueSearchMapper searchMapper;


    public Page<VenueSearchResponse> searchVenues(String keyword,
                                                  Pageable pageable) {

        VenueSearchResult result = searchRepository.search(keyword, pageable.getPageNumber(), pageable.getPageSize());

        List<VenueSearchResponse> content =
                result.getDocuments()
                        .stream()
                        .map(searchMapper::toVenueSearchResponse)
                        .toList();

        return new PageImpl<>(content, pageable, result.getTotalElements());

    }


}