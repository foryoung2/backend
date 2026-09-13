package com.foryoung.foryoung.performance.setlist.repository;

import com.foryoung.foryoung.performance.schedule.entity.PerformanceSchedule;
import com.foryoung.foryoung.performance.setlist.entity.Setlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SetlistRepository extends JpaRepository<Setlist, Long> {


    List<Setlist> findByPerformanceScheduleOrderByTrackNumberAsc(PerformanceSchedule performanceSchedule);


    @Query("""
            select coalesce(max(ps.trackNumber), 0)
            from Setlist ps
            where ps.performanceSchedule = :schedule
            """)
    Integer findMaxTrackNumber(@Param("schedule") PerformanceSchedule schedule);


    @Modifying
    @Query("""
            update Setlist ps
            set ps.trackNumber = ps.trackNumber - 1
            where ps.performanceSchedule = :schedule
              and ps.trackNumber > :trackNumber
            """)
    void decreaseTrackNumbers(
            @Param("schedule") PerformanceSchedule schedule,
            @Param("trackNumber") Integer trackNumber
    );


}