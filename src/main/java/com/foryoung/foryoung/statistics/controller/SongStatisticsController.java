package com.foryoung.foryoung.statistics.controller;

import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.statistics.dto.SongStatisticsResponse;
import com.foryoung.foryoung.statistics.service.SongStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performance-statistics/setlist")
public class SongStatisticsController {


    private final SongStatisticsService songStatisticsService;


    @GetMapping
    public ResponseEntity<SongStatisticsResponse> getSongStatistics(@AuthenticationPrincipal CustomUserDetails userDetails) {

        SongStatisticsResponse response = songStatisticsService.getSongStatistics(userDetails.getMemberId());

        return ResponseEntity.ok(response);

    }


}