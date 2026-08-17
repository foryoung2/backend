package com.foryoung.foryoung.report.controller;

import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.report.dto.AttendanceCalendarResponse;
import com.foryoung.foryoung.report.dto.PerformanceReportResponse;
import com.foryoung.foryoung.report.service.PerformanceReportService;
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
@RequestMapping("/performances/reports")
public class PerformanceReportController {


    private final PerformanceReportService reportService;


    @GetMapping("/monthly")
    public ResponseEntity<PerformanceReportResponse> getMonthlyReport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @RequestParam int year,
                                                                      @RequestParam int month) {

        return ResponseEntity.ok(
                reportService.getMonthlyPerformanceReport(userDetails.getMemberId(), year, month)
        );

    }


    @GetMapping("/yearly")
    public ResponseEntity<PerformanceReportResponse> getYearlyReport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @RequestParam int year) {

        return ResponseEntity.ok(
                reportService.getYearlyPerformanceReport(userDetails.getMemberId(), year)
        );

    }


    @GetMapping("/calendar")
    public ResponseEntity<List<AttendanceCalendarResponse>> calendar(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @RequestParam int year,
                                                                     @RequestParam int month) {

        return ResponseEntity.ok(
                reportService.getAttendanceCalendar(userDetails.getMemberId(), year, month)
        );

    }


}