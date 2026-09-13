package com.foryoung.foryoung.statistics.service;

import com.foryoung.foryoung.statistics.dto.SongStatisticsResponse;
import com.foryoung.foryoung.statistics.dto.SongFrequencyResponse;
import com.foryoung.foryoung.statistics.repository.SongStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SongStatisticsService {


    private static final int TOP_SONG_LIMIT = 10;

    private final SongStatisticsRepository songStatisticsRepository;


    public SongStatisticsResponse getSongStatistics(Long memberId) {

        long totalPerformances = songStatisticsRepository.countTotalPerformances(memberId);

        long performanceWithSetlists = songStatisticsRepository.countPerformancesWithSetlist(memberId);

        long totalSongOccurrences = songStatisticsRepository.countTotalSongOccurrences(memberId);

        List<SongFrequencyResponse> topSongs = songStatisticsRepository.findTopSongs(memberId)
                .stream()
                .limit(TOP_SONG_LIMIT)
                .toList();

        return SongStatisticsResponse.builder()
                .totalPerformances(totalPerformances)
                .performancesWithSetlist(performanceWithSetlists)
                .totalSongOccurrences(totalSongOccurrences)
                .topSongs(topSongs)
                .build();

    }


}