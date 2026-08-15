package com.foryoung.foryoung.search.controller;

import com.foryoung.foryoung.search.dto.PerformanceSearchResponse;
import com.foryoung.foryoung.search.service.PerformanceSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performances/search")
public class PerformanceSearchController {


    private final PerformanceSearchService searchService;


    @GetMapping
    public ResponseEntity<List<PerformanceSearchResponse>> searchPerformances(@RequestParam String keyword) {

        return ResponseEntity.ok(searchService.searchPerformances(keyword));

    }


}