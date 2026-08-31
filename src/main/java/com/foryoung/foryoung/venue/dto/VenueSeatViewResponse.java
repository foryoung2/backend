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

    private String writerNickname;

    private boolean owner;

    private String seatInfo;

    private String content;

    private List<String> imageUrls;


}