package com.foryoung.foryoung.venue.dto;

import com.foryoung.foryoung.venue.entity.Venue;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VenueSearchResponse {


    private Long id;
    private String name;


    public static VenueSearchResponse from(Venue venue) {

        return VenueSearchResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .build();

    }


}