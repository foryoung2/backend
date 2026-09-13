package com.foryoung.foryoung.statistics.repository;

import com.foryoung.foryoung.performance.record.entity.PerformanceRecord;
import com.foryoung.foryoung.statistics.dto.SongFrequencyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongStatisticsRepository extends JpaRepository<PerformanceRecord, Long> {


    @Query("""
            select count(pr)
            from PerformanceRecord pr
            where pr.member.id = :memberId
            """)
    long countTotalPerformances(@Param("memberId") Long memberId);


    @Query("""
            select count(distinct ps.id)
            from PerformanceRecord pr
            join pr.schedule ps
            where pr.member.id = :memberId
              and exists (
                  select 1
                  from Setlist sl
                  where sl.performanceSchedule = ps
              )
            """)
    long countPerformancesWithSetlist(@Param("memberId") Long memberId);


    @Query("""
            select count(distinct sl.id)
            from PerformanceRecord pr
            join pr.schedule ps
            join Setlist sl
                on sl.performanceSchedule = ps
            where pr.member.id = :memberId
            """)
    long countTotalSongOccurrences(@Param("memberId") Long memberId);


    @Query("""
            select new com.foryoung.foryoung.statistics.dto.SongStatisticsResponse(
                song.id,
                song.title,
                song.artist,
                count(distinct ps.id)
            )
            from PerformanceRecord pr
            join pr.schedule ps
            join Setlist sl
                on sl.performanceSchedule = ps
            join sl.song song
            where pr.member.id = :memberId
            group by song.id, song.title, song.artist
            order by count(distinct ps.id) desc
            """)
    List<SongFrequencyResponse> findTopSongs(@Param("memberId") Long memberId);


}