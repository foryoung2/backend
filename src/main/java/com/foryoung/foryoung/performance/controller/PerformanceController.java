package com.foryoung.foryoung.performance.controller;

import com.foryoung.foryoung.global.pagination.PageResponse;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PerformanceResponse> createPerformance(@Valid @RequestPart("request") PerformanceCreateRequest request,
                                                                 @RequestPart(value = "posterImage", required = false) MultipartFile posterImage) {

        PerformanceResponse performanceResponse = performanceService.createPerformance(request, posterImage);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(performanceResponse);

    }


    @GetMapping
    public ResponseEntity<PageResponse<PerformanceResponse>> getPerformances(@PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC)
                                                                                 Pageable pageable) {

        Page<PerformanceResponse> response = performanceService.getPerformances(pageable);

        return ResponseEntity.ok(PageResponse.from(response));

    }


    @GetMapping("/{performanceId}")
    public ResponseEntity<PerformanceDetailResponse> getPerformance(@PathVariable Long performanceId) {

        return ResponseEntity.ok(performanceService.getPerformance(performanceId));

    }


}