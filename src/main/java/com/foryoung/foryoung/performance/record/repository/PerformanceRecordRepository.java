package com.foryoung.foryoung.performance.record.repository;

import com.foryoung.foryoung.performance.record.entity.PerformanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PerformanceRecordRepository extends JpaRepository<PerformanceRecord, Long>, PerformanceRecordRepositoryCustom {


    Optional<PerformanceRecord> findByIdAndMember_Id(Long performanceRecordId, Long memberId);


    Page<PerformanceRecord> findByMember_IdOrderBySchedule_PerformanceDateTimeDesc(
            Long memberId,
            Pageable pageable
    );


    @Query("""
            select avg(pr.rating)
            from PerformanceRecord pr
            where pr.schedule.performance.id = :performanceId
              and pr.rating is not null
            """)
    Double findAverageRating(@Param("performanceId") Long performanceId);


    @Query("""
            select count(pr)
            from PerformanceRecord pr
            where pr.schedule.performance.id = :performanceId
              and pr.rating is not null
            """)
    long countRatedRecords(@Param("performanceId") Long performanceId);


}