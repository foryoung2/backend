package com.foryoung.foryoung.venue.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VenueSeatViewResponse {


    private Long id;

    private Long venueId;

    private String venueName;

    private String seatInfo;

    private String description;

    private List<String> imageUrls;


}