package com.foryoung.foryoung.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PerformanceStatisticsSummary {


    private long attendanceCount;

    private long totalSpent;

    private Integer highestTicketPrice;

    private Integer lowestTicketPrice;


    public static PerformanceStatisticsSummary empty() {

        return new PerformanceStatisticsSummary(
                0L, 0L, null, null
        );

    }


}