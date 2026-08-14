package com.foryoung.foryoung.performance.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.performance.dto.PerformanceCreateRequest;
import com.foryoung.foryoung.performance.dto.PerformanceDetailResponse;
import com.foryoung.foryoung.performance.dto.PerformanceResponse;
import com.foryoung.foryoung.performance.dto.PerformanceScheduleResponse;
import com.foryoung.foryoung.performance.entity.Performance;
import com.foryoung.foryoung.performance.mapper.PerformanceMapper;
import com.foryoung.foryoung.performance.repository.PerformanceRepository;
import com.foryoung.foryoung.performance.repository.PerformanceScheduleRepository;
import com.foryoung.foryoung.search.document.PerformanceDocument;
import com.foryoung.foryoung.search.mapper.PerformanceSearchMapper;
import com.foryoung.foryoung.search.repository.PerformanceSearchRepository;
import com.foryoung.foryoung.venue.entity.Venue;
import com.foryoung.foryoung.venue.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceService {


    private final PerformanceRepository performanceRepository;
    private final PerformanceScheduleRepository scheduleRepository;
    private final PerformanceSearchRepository searchRepository;
    private final VenueRepository venueRepository;

    private final PerformanceMapper performanceMapper;
    private final PerformanceSearchMapper searchMapper;


    @Transactional
    public PerformanceResponse createPerformance(PerformanceCreateRequest request) {

        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new CustomException(ErrorCode.VENUE_NOT_FOUND));

        Performance performance = Performance.builder()
                .title(request.getTitle())
                .artist(request.getArtist())
                .venue(venue)
                .posterImageUrl(request.getPosterImageUrl())
                .build();

        Performance savedPerformance = performanceRepository.save(performance);

        PerformanceDocument document = searchMapper.toPerformanceDocument(savedPerformance);

        searchRepository.save(document);

        return performanceMapper.toPerformanceResponse(savedPerformance);

    }


    public Page<PerformanceResponse> getPerformances(Pageable pageable) {

        return performanceRepository
                .findAll(pageable)
                .map(performanceMapper::toPerformanceResponse);
        
    }


    public PerformanceDetailResponse getPerformance(Long performanceId) {

        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_NOT_FOUND));

        List<PerformanceScheduleResponse> schedules = scheduleRepository
                .findByPerformanceOrderByPerformanceDateTimeAsc(performance)
                .stream()
                .map(PerformanceScheduleResponse::from)
                .toList();

        return PerformanceDetailResponse.builder()
                .id(performance.getId())
                .title(performance.getTitle())
                .artist(performance.getArtist())
                .venue(performance.getVenue().getName())
                .posterImageUrl(performance.getPosterImageUrl())
                .schedules(schedules)
                .build();

    }


}