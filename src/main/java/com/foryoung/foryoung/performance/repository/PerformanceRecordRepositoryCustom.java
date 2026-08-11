package com.foryoung.foryoung.performance.repository;

import com.foryoung.foryoung.report.dto.AttendanceCalendarResponse;
import com.foryoung.foryoung.report.dto.FrequencyResult;
import com.foryoung.foryoung.report.dto.PerformanceReportSummary;

import java.time.LocalDateTime;
import java.util.List;

public interface PerformanceRecordRepositoryCustom {


    PerformanceReportSummary findReportSummary(
            Long memberId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );


    List<FrequencyResult> findMostAttendedPerformances(
            Long memberId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );


    List<FrequencyResult> findMostVisitedVenues(
            Long memberId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );


    List<AttendanceCalendarResponse> findAttendanceCalendar(
            Long memberId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );


}