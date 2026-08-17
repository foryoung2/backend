package com.foryoung.foryoung.report.service;

import com.foryoung.foryoung.performance.repository.PerformanceRecordRepository;
import com.foryoung.foryoung.report.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceReportService {


    private final PerformanceRecordRepository recordRepository;


    public PerformanceReportResponse getMonthlyPerformanceReport(Long memberId,
                                                                 int year,
                                                                 int month) {

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDateTime startTime = yearMonth
                .atDay(1)
                .atStartOfDay();

        LocalDateTime endTime = yearMonth
                .plusMonths(1)
                .atDay(1)
                .atStartOfDay();

        PerformanceReportSummary summary =
                Optional.ofNullable(
                        recordRepository.findReportSummary(memberId, startTime, endTime)
                ).orElseGet(PerformanceReportSummary::empty);

        List<FrequencyResult> performances =
                recordRepository.findMostAttendedPerformances(memberId, startTime, endTime);

        List<FrequencyResult> venues =
                recordRepository.findMostVisitedVenues(memberId, startTime, endTime);

        return createReport(year, month, summary, performances, venues);

    }


    public PerformanceReportResponse getYearlyPerformanceReport(Long memberId, int year) {

        LocalDateTime startTime = LocalDate.of(year, 1, 1)
                        .atStartOfDay();

        LocalDateTime endTime = LocalDate.of(year + 1, 1, 1)
                        .atStartOfDay();

        PerformanceReportSummary summary =
                Optional.ofNullable(
                        recordRepository.findReportSummary(memberId, startTime, endTime)
                ).orElseGet(PerformanceReportSummary::empty);

        List<FrequencyResult> performances =
                recordRepository.findMostAttendedPerformances(memberId, startTime, endTime);

        List<FrequencyResult> venues =
                recordRepository.findMostVisitedVenues(memberId, startTime, endTime);

        return createReport(year, null, summary, performances, venues);

    }


    public List<AttendanceCalendarResponse> getAttendanceCalendar(Long memberId,
                                                                  int year,
                                                                  int month) {

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDateTime startTime = yearMonth
                .atDay(1)
                .atStartOfDay();

        LocalDateTime endTime = yearMonth
                .plusMonths(1)
                .atDay(1)
                .atStartOfDay();

        return recordRepository.findAttendanceCalendar(memberId, startTime, endTime);

    }


    private PerformanceReportResponse createReport(int year,
                                                   Integer month,
                                                   PerformanceReportSummary summary,
                                                   List<FrequencyResult> performances,
                                                   List<FrequencyResult> venues) {

        MostFrequentResult mostAttendedPerformance = findMostFrequent(performances);

        MostFrequentResult mostVisitedVenue = findMostFrequent(venues);

        return PerformanceReportResponse.builder()
                .year(year)
                .month(month)
                .attendanceCount(summary.getAttendanceCount())
                .totalSpent(summary.getTotalSpent())
                .highestTicketPrice(summary.getHighestTicketPrice())
                .lowestTicketPrice(summary.getLowestTicketPrice())
                .mostAttendedPerformance(mostAttendedPerformance.getName())
                .mostVisitedVenue(mostVisitedVenue.getName())
                .mostAttendedPerformanceTieCount(mostAttendedPerformance.getTieCount())
                .mostVisitedVenueTieCount(mostVisitedVenue.getTieCount())
                .build();

    }


    private MostFrequentResult findMostFrequent(List<FrequencyResult> results) {

        if (results.isEmpty()) {
            return new MostFrequentResult(null, 0);
        }

        long maxCount =
                results.stream()
                        .mapToLong(FrequencyResult::getCount)
                        .max()
                        .orElse(0L);

        List<String> topNames =
                results.stream()
                        .filter(result ->
                                result.getCount() == maxCount
                        )
                        .map(FrequencyResult::getName)
                        .sorted()
                        .toList();

        return new MostFrequentResult(
                topNames.getFirst(),
                topNames.size()
        );

    }


}