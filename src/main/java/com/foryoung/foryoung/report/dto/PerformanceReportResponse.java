package com.foryoung.foryoung.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PerformanceReportResponse {


    private int year;

    private Integer month;

    private long attendanceCount;

    private long totalSpent;

    private Integer highestTicketPrice;

    private Integer lowestTicketPrice;

    private String mostAttendedPerformance;

    private String mostVisitedVenue;

    private int mostAttendedPerformanceTieCount;

    private int mostVisitedVenueTieCount;


}