package com.foryoung.foryoung.review.mapper;

import com.foryoung.foryoung.review.dto.LikedReviewResponse;
import com.foryoung.foryoung.review.entity.ReviewLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewLikeMapper {


    @Mapping(target = "reviewId", source = "performanceReview.id")
    @Mapping(target = "reviewTitle", source = "performanceReview.title")
    @Mapping(
            target = "performanceTitle",
            source = "performanceReview.performanceRecord.schedule.performance.title"
    )
    @Mapping(
            target = "writerNickname",
            expression =
                    "java(reviewLike.getPerformanceReview().getPerformanceRecord().getMember().getDisplayNickname())"
    )
    @Mapping(target = "likedAt", source = "createdAt")
    LikedReviewResponse toLikedReviewResponse(ReviewLike reviewLike);


}