package com.foryoung.foryoung.statistics.controller;

import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.statistics.dto.AttendanceCalendarResponse;
import com.foryoung.foryoung.statistics.dto.PerformanceStatisticsResponse;
import com.foryoung.foryoung.statistics.service.PerformanceStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performance-statistics")
public class PerformanceStatisticsController {


    private final PerformanceStatisticsService reportService;


    @GetMapping("/monthly")
    public ResponseEntity<PerformanceStatisticsResponse> getMonthlyPerformanceStatistics(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                         @RequestParam int year,
                                                                                         @RequestParam int month) {

        return ResponseEntity.ok(
                reportService.getMonthlyPerformanceStatistics(userDetails.getMemberId(), year, month)
        );

    }


    @GetMapping("/yearly")
    public ResponseEntity<PerformanceStatisticsResponse> getYearlyPerformanceStatistics(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                        @RequestParam int year) {

        return ResponseEntity.ok(
                reportService.getYearlyPerformanceStatistics(userDetails.getMemberId(), year)
        );

    }


    @GetMapping("/calendar")
    public ResponseEntity<List<AttendanceCalendarResponse>> getAttendanceCalendar(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                  @RequestParam int year,
                                                                                  @RequestParam int month) {

        return ResponseEntity.ok(
                reportService.getAttendanceCalendar(userDetails.getMemberId(), year, month)
        );

    }


}