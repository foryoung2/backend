package com.foryoung.foryoung.performance.record.repository;

import com.foryoung.foryoung.statistics.dto.AttendanceCalendarResponse;
import com.foryoung.foryoung.statistics.dto.FrequencyResult;
import com.foryoung.foryoung.statistics.dto.PerformanceStatisticsSummary;

import java.time.LocalDateTime;
import java.util.List;

public interface PerformanceRecordRepositoryCustom {


    PerformanceStatisticsSummary findReportSummary(
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