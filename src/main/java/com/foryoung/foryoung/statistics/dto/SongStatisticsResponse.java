package com.foryoung.foryoung.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SongStatisticsResponse {


    private long totalPerformances;

    private long performancesWithSetlist;

    private long totalSongOccurrences;

    private List<SongFrequencyResponse> topSongs;


}