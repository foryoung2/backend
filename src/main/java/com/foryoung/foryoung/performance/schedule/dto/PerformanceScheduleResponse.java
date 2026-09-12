package com.foryoung.foryoung.performance.schedule.dto;

import com.foryoung.foryoung.performance.schedule.entity.PerformanceSchedule;

import com.foryoung.foryoung.performance.setlist.dto.SetlistResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PerformanceScheduleResponse {


    private Long id;

    private LocalDateTime performanceDateTime;

    private List<SetlistResponse> setlists;


    public static PerformanceScheduleResponse from(PerformanceSchedule schedule,
                                                   List<SetlistResponse> setlists) {

        return new PerformanceScheduleResponse(
                schedule.getId(),
                schedule.getPerformanceDateTime(),
                setlists
        );

    }


}