package com.foryoung.foryoung.performance.repository;

import com.foryoung.foryoung.performance.entity.QPerformance;
import com.foryoung.foryoung.performance.entity.QPerformanceRecord;
import com.foryoung.foryoung.performance.entity.QPerformanceSchedule;
import com.foryoung.foryoung.report.dto.AttendanceCalendarResponse;
import com.foryoung.foryoung.report.dto.FrequencyResult;
import com.foryoung.foryoung.report.dto.PerformanceReportSummary;
import com.foryoung.foryoung.venue.entity.QVenue;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PerformanceRecordRepositoryImpl implements PerformanceRecordRepositoryCustom {


    private final JPAQueryFactory queryFactory;


    @Override
    public PerformanceReportSummary findReportSummary(Long memberId,
                                                      LocalDateTime startTime,
                                                      LocalDateTime endTime) {

        QPerformanceRecord record = QPerformanceRecord.performanceRecord;

        return queryFactory
                .select(
                        Projections.constructor(
                                PerformanceReportSummary.class,
                                record.count(),
                                record.ticketPrice.sum().longValue(),
                                record.ticketPrice.max(),
                                record.ticketPrice.min()
                        )
                )
                .from(record)

                .where(
                        record.member.id.eq(memberId),
                        record.schedule.performanceDateTime.goe(startTime),
                        record.schedule.performanceDateTime.lt(endTime)
                )

                .fetchOne();

    }


    @Override
    public List<FrequencyResult> findMostAttendedPerformances(Long memberId,
                                                              LocalDateTime startTime,
                                                              LocalDateTime endTime) {

        QPerformanceRecord record = QPerformanceRecord.performanceRecord;
        QPerformanceSchedule schedule = QPerformanceSchedule.performanceSchedule;
        QPerformance performance = QPerformance.performance;

        return queryFactory
                .select(
                        Projections.constructor(
                                FrequencyResult.class,
                                performance.title,
                                record.count()
                        )
                )
                .from(record)

                .join(record.schedule, schedule)

                .join(schedule.performance, performance)

                .where(
                        record.member.id.eq(memberId),
                        schedule.performanceDateTime.goe(startTime),
                        schedule.performanceDateTime.lt(endTime)
                )

                .groupBy(performance.id, performance.title)

                .orderBy(record.count().desc())

                .fetch();

    }


    @Override
    public List<FrequencyResult> findMostVisitedVenues(Long memberId,
                                                       LocalDateTime startTime,
                                                       LocalDateTime endTime) {

        QPerformanceRecord record = QPerformanceRecord.performanceRecord;
        QPerformanceSchedule schedule = QPerformanceSchedule.performanceSchedule;
        QPerformance performance = QPerformance.performance;
        QVenue venue = QVenue.venue;

        return queryFactory
                .select(
                        Projections.constructor(
                                FrequencyResult.class,
                                venue.name,
                                record.count()
                        )
                )
                .from(record)

                .join(record.schedule, schedule)

                .join(schedule.performance, performance)

                .join(performance.venue, venue)

                .where(
                        record.member.id.eq(memberId),
                        schedule.performanceDateTime.goe(startTime),
                        schedule.performanceDateTime.lt(endTime)
                )

                .groupBy(venue.id, venue.name)

                .orderBy(record.count().desc())

                .fetch();

    }


    @Override
    public List<AttendanceCalendarResponse> findAttendanceCalendar(Long memberId,
                                                                   LocalDateTime startTime,
                                                                   LocalDateTime endTime) {

        QPerformanceRecord record = QPerformanceRecord.performanceRecord;
        QPerformanceSchedule schedule = QPerformanceSchedule.performanceSchedule;
        QPerformance performance = QPerformance.performance;
        QVenue venue = QVenue.venue;

        List<Tuple> results = queryFactory
                .select(
                        schedule.performanceDateTime,
                        performance.id,
                        performance.title,
                        performance.artist,
                        venue.name
                )
                .from(record)

                .join(record.schedule, schedule)

                .join(schedule.performance, performance)

                .join(performance.venue, venue)

                .where(
                        record.member.id.eq(memberId),
                        schedule.performanceDateTime.goe(startTime),
                        schedule.performanceDateTime.lt(endTime)
                )

                .orderBy(schedule.performanceDateTime.asc())

                .fetch();

        return results.stream()
                .map(tuple -> new AttendanceCalendarResponse(
                        tuple.get(schedule.performanceDateTime).toLocalDate(),
                        tuple.get(performance.id),
                        tuple.get(performance.title),
                        tuple.get(performance.artist),
                        tuple.get(venue.name)
                ))
                .toList();
        
    }

}