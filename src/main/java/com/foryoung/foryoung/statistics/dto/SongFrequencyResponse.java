package com.foryoung.foryoung.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SongFrequencyResponse {


    private Long songId;

    private String title;

    private String artist;

    private Long count;


}