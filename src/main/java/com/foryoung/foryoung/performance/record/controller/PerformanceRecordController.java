package com.foryoung.foryoung.performance.controller;

import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.global.pagination.PageResponse;
import com.foryoung.foryoung.performance.dto.PerformanceRecordCreateRequest;
import com.foryoung.foryoung.performance.dto.PerformanceRecordResponse;
import com.foryoung.foryoung.performance.dto.PerformanceRecordUpdateRequest;
import com.foryoung.foryoung.performance.service.PerformanceRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performance-records")
public class PerformanceRecordController {


    private final PerformanceRecordService recordService;


    @PostMapping
    public ResponseEntity<PerformanceRecordResponse> createRecord(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @Valid @RequestBody PerformanceRecordCreateRequest request) {

        recordService.createRecord(userDetails.getMemberId(), request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();

    }


    @PatchMapping("/{recordId}")
    public ResponseEntity<Void> updateRecord(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Long recordId,
                                             @Valid @RequestBody PerformanceRecordUpdateRequest request) {

        recordService.updateRecord(userDetails.getMemberId(), recordId, request);

        return ResponseEntity.ok().build();

    }


    @GetMapping("/me")
    public ResponseEntity<PageResponse<PerformanceRecordResponse>> getMyRecords(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                @PageableDefault(size = 12, sort = "schedule.performanceDateTime", direction = Sort.Direction.DESC)
                                                                                Pageable pageable) {

        Page<PerformanceRecordResponse> responses =
                recordService.getMyRecords(userDetails.getMemberId(), pageable);

        return ResponseEntity.ok(PageResponse.from(responses));

    }


    @GetMapping("/{recordId}")
    public ResponseEntity<PerformanceRecordResponse> getMyRecord(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable Long recordId) {

        PerformanceRecordResponse record = recordService.getMyRecord(userDetails.getMemberId(), recordId);

        return ResponseEntity.ok(record);

    }


    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteRecord(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Long recordId) {

        recordService.deleteRecord(userDetails.getMemberId(), recordId);

        return ResponseEntity.noContent().build();

    }


}