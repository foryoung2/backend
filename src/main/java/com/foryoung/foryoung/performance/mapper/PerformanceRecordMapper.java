package com.foryoung.foryoung.performance.mapper;

import com.foryoung.foryoung.performance.dto.PerformanceRecordResponse;
import com.foryoung.foryoung.performance.entity.PerformanceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerformanceRecordMapper {


    @Mapping(target = "performanceId", source = "schedule.performance.id")
    @Mapping(target = "performanceTitle", source = "schedule.performance.title")
    @Mapping(target = "artist", source = "schedule.performance.artist")
    @Mapping(target = "venue", source = "schedule.performance.venue.name")
    @Mapping(target = "scheduleId", source = "schedule.id")
    @Mapping(target = "performanceDateTime", source = "schedule.performanceDateTime")
    PerformanceRecordResponse toPerformanceRecordResponse(PerformanceRecord record);


}