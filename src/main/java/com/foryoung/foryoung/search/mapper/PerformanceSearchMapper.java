package com.foryoung.foryoung.search.mapper;

import com.foryoung.foryoung.performance.entity.Performance;
import com.foryoung.foryoung.search.document.PerformanceDocument;
import com.foryoung.foryoung.search.dto.PerformanceSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerformanceSearchMapper {


    @Mapping(target = "venue", source = "venue.name")
    PerformanceDocument toPerformanceDocument(Performance performance);


    PerformanceSearchResponse toPerformanceSearchResponse(PerformanceDocument document);


}