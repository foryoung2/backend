package com.foryoung.foryoung.performance.controller;

import com.foryoung.foryoung.performance.dto.PerformanceCreateRequest;
import com.foryoung.foryoung.performance.dto.PerformanceDetailResponse;
import com.foryoung.foryoung.performance.dto.PerformanceResponse;
import com.foryoung.foryoung.performance.service.PerformanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performances")
public class PerformanceController {


    private final PerformanceService performanceService;


    @PostMapping
    public ResponseEntity<PerformanceResponse> createPerformance(@Valid @RequestBody PerformanceCreateRequest request) {

        PerformanceResponse performanceResponse = performanceService.createPerformance(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(performanceResponse);

    }


    @GetMapping
    public ResponseEntity<Page<PerformanceResponse>> getPerformances(@PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC)
                                                                         Pageable pageable) {

        return ResponseEntity.ok(
                performanceService.getPerformances(pageable)
        );

    }


    @GetMapping("/{performanceId}")
    public ResponseEntity<PerformanceDetailResponse> getPerformance(@PathVariable Long performanceId) {

        return ResponseEntity.ok(performanceService.getPerformance(performanceId));

    }


}