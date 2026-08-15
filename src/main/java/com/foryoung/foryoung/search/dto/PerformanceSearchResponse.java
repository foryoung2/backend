package com.foryoung.foryoung.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PerformanceSearchResponse {


    private Long id;

    private String title;

    private String artist;

    private String venue;

    private String posterImageUrl;


}