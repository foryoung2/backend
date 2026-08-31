package com.foryoung.foryoung.performance.dto;

import com.foryoung.foryoung.performance.entity.Performance;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PerformanceDetailResponse {


    private Long id;

    private String title;

    private String artist;

    private Long venueId;

    private String venue;

    private String posterImageUrl;

    private List<PerformanceScheduleResponse> schedules;


    public static PerformanceDetailResponse from(Performance performance,
                                                 List<PerformanceScheduleResponse> schedules) {

        return PerformanceDetailResponse.builder()
                .id(performance.getId())
                .title(performance.getTitle())
                .artist(performance.getArtist())
                .venueId(performance.getVenue().getId())
                .venue(performance.getVenue().getName())
                .posterImageUrl(performance.getPosterImageUrl())
                .schedules(schedules)
                .build();

    }


}