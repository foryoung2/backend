package com.foryoung.foryoung.performance.repository;

import com.foryoung.foryoung.performance.entity.PerformanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerformanceRecordRepository extends JpaRepository<PerformanceRecord, Long>, PerformanceRecordRepositoryCustom {


    Optional<PerformanceRecord> findByIdAndMember_Id(Long performanceRecordId, Long memberId);


    Page<PerformanceRecord> findByMember_IdOrderBySchedule_PerformanceDateTimeDesc(
            Long memberId,
            Pageable pageable
    );


}