package com.foryoung.foryoung.performance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PerformanceResponse {


    private Long id;

    private String title;

    private String artist;

    private String venue;

    private String posterImageUrl;


}