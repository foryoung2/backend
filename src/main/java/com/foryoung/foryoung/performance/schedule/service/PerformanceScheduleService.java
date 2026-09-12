package com.foryoung.foryoung.performance.schedule.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.performance.schedule.dto.PerformanceScheduleCreateRequest;
import com.foryoung.foryoung.performance.schedule.dto.PerformanceScheduleResponse;
import com.foryoung.foryoung.performance.performance.entity.Performance;
import com.foryoung.foryoung.performance.schedule.entity.PerformanceSchedule;
import com.foryoung.foryoung.performance.performance.repository.PerformanceRepository;
import com.foryoung.foryoung.performance.schedule.repository.PerformanceScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceScheduleService {


    private final PerformanceRepository performanceRepository;
    private final PerformanceScheduleRepository scheduleRepository;


    @Transactional
    public PerformanceScheduleResponse createSchedule(Long performanceId,
                                                      PerformanceScheduleCreateRequest request) {

        Performance performance =
                performanceRepository.findById(performanceId)
                        .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_NOT_FOUND));

        PerformanceSchedule schedule = PerformanceSchedule.builder()
                .performance(performance)
                .performanceDateTime(request.getPerformanceDateTime())
                .build();

        PerformanceSchedule savedSchedule = scheduleRepository.save(schedule);

        return PerformanceScheduleResponse.from(savedSchedule, List.of());

    }


}