package com.foryoung.foryoung.ocr.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PerformanceOcrResponse {


    private String title;

    private String venue;

    private LocalDateTime performanceDateTime;

    private Integer ticketPrice;

    private String seat;

    //private String rawText


}