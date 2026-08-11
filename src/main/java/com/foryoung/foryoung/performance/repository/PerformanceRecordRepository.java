package com.foryoung.foryoung.performance.repository;

import com.foryoung.foryoung.performance.entity.PerformanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerformanceRecordRepository extends JpaRepository<PerformanceRecord, Long>, PerformanceRecordRepositoryCustom {


    List<PerformanceRecord> findByMember_IdOrderBySchedule_PerformanceDateTimeDesc(Long memberId);


    Optional<PerformanceRecord> findByIdAndMember_Id(Long performanceRecordId, Long memberId);


}