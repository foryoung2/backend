package com.foryoung.foryoung.venue.dto;

import com.foryoung.foryoung.venue.entity.Venue;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VenueResponse {


    private Long id;

    private String name;


    public static VenueResponse from(Venue venue) {

        return VenueResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .build();

    }


}