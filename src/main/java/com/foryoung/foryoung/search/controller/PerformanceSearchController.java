package com.foryoung.foryoung.search.controller;

import com.foryoung.foryoung.global.pagination.PageResponse;
import com.foryoung.foryoung.search.dto.PerformanceSearchResponse;
import com.foryoung.foryoung.search.service.PerformanceSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performances/search")
public class PerformanceSearchController {


    private final PerformanceSearchService searchService;


    @GetMapping
    public ResponseEntity<PageResponse<PerformanceSearchResponse>> searchPerformances(@RequestParam String keyword,
                                                                                      @PageableDefault(size = 12) Pageable pageable) {

        Page<PerformanceSearchResponse> responses = searchService.searchPerformances(keyword, pageable);

        return ResponseEntity.ok(PageResponse.from(responses));

    }


}