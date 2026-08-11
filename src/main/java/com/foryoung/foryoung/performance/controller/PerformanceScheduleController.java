package com.foryoung.foryoung.performance.controller;

import com.foryoung.foryoung.performance.dto.PerformanceScheduleCreateRequest;
import com.foryoung.foryoung.performance.dto.PerformanceScheduleResponse;
import com.foryoung.foryoung.performance.service.PerformanceScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performances")
public class PerformanceScheduleController {


    private final PerformanceScheduleService scheduleService;


    @PostMapping("/{performanceId}/schedules")
    public ResponseEntity<PerformanceScheduleResponse> createSchedule(@PathVariable Long performanceId,
                                                                      @Valid @RequestBody PerformanceScheduleCreateRequest request) {

        PerformanceScheduleResponse response = scheduleService.createSchedule(performanceId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


}