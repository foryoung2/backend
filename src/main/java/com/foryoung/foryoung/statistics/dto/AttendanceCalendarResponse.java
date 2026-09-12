package com.foryoung.foryoung.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class AttendanceCalendarResponse {


    private LocalDate attendanceDate;

    private Long performanceId;

    private String title;

    private String artist;

    private String venue;


}