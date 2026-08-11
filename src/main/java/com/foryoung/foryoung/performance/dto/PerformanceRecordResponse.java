package com.foryoung.foryoung.performance.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PerformanceRecordResponse {


    private Long id;

    private Long performanceId;

    private String performanceTitle;

    private String artist;

    private String venue;

    private Long scheduleId;

    private LocalDateTime performanceDateTime;

    private Integer ticketPrice;

    private String seat;


}