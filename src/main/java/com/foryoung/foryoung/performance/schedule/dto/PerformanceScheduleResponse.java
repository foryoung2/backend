package com.foryoung.foryoung.performance.dto;

import com.foryoung.foryoung.performance.entity.PerformanceSchedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PerformanceScheduleResponse {


    private Long id;

    private LocalDateTime performanceDateTime;


    public static PerformanceScheduleResponse from(PerformanceSchedule schedule) {

        return new PerformanceScheduleResponse(
                schedule.getId(),
                schedule.getPerformanceDateTime()
        );

    }


}