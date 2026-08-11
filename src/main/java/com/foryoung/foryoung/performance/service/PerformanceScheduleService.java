package com.foryoung.foryoung.performance.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.performance.dto.PerformanceScheduleCreateRequest;
import com.foryoung.foryoung.performance.dto.PerformanceScheduleResponse;
import com.foryoung.foryoung.performance.entity.Performance;
import com.foryoung.foryoung.performance.entity.PerformanceSchedule;
import com.foryoung.foryoung.performance.repository.PerformanceRepository;
import com.foryoung.foryoung.performance.repository.PerformanceScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return PerformanceScheduleResponse.from(savedSchedule);

    }


}