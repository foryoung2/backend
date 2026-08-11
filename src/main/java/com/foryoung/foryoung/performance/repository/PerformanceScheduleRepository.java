package com.foryoung.foryoung.performance.repository;

import com.foryoung.foryoung.performance.entity.Performance;
import com.foryoung.foryoung.performance.entity.PerformanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceScheduleRepository extends JpaRepository<PerformanceSchedule, Long> {


    List<PerformanceSchedule> findByPerformanceOrderByPerformanceDateTimeAsc(Performance performance);


}