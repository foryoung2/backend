package com.foryoung.foryoung.performance.dto;

import com.foryoung.foryoung.performance.entity.Performance;
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


    public static PerformanceResponse from(Performance performance) {

        return PerformanceResponse.builder()
                .id(performance.getId())
                .title(performance.getTitle())
                .artist(performance.getArtist())
                .venue(performance.getVenue().getName())
                .posterImageUrl(performance.getPosterImageUrl())
                .build();

    }


}