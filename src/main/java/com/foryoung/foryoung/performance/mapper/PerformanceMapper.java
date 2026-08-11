package com.foryoung.foryoung.performance.mapper;

import com.foryoung.foryoung.performance.dto.PerformanceResponse;
import com.foryoung.foryoung.performance.entity.Performance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerformanceMapper {


    @Mapping(target = "venue", source = "venue.name")
    PerformanceResponse toPerformanceResponse(Performance performance);


}