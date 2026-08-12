package com.foryoung.foryoung.review.mapper;

import com.foryoung.foryoung.review.dto.PerformanceReviewResponse;
import com.foryoung.foryoung.review.entity.PerformanceReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerformanceReviewMapper {


    @Mapping(target = "performanceRecordId", source = "performanceRecord.id")
    @Mapping(
            target = "writerNickname",
            expression = "java(review.getPerformanceRecord().getMember().getDisplayNickname())"
    )
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "liked", ignore = true)
    PerformanceReviewResponse toPerformanceReviewResponse(PerformanceReview review);


}