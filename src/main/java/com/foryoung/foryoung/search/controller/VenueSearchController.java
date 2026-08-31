package com.foryoung.foryoung.search.controller;

import com.foryoung.foryoung.global.pagination.PageResponse;
import com.foryoung.foryoung.search.dto.VenueSearchResponse;
import com.foryoung.foryoung.search.service.VenueSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venues/search")
public class VenueSearchController {


    private final VenueSearchService searchService;


    @GetMapping
    public ResponseEntity<PageResponse<VenueSearchResponse>> searchVenues(@RequestParam String keyword,
                                                                          @PageableDefault(size = 12) Pageable pageable) {

        Page<VenueSearchResponse> responses = searchService.searchVenues(keyword, pageable);

        return ResponseEntity.ok(PageResponse.from(responses));

    }


}