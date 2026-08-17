package com.foryoung.foryoung.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PerformanceReportSummary {


    private long attendanceCount;

    private long totalSpent;

    private Integer highestTicketPrice;

    private Integer lowestTicketPrice;


    public static PerformanceReportSummary empty() {

        return new PerformanceReportSummary(
                0L, 0L, null, null
        );

    }


}