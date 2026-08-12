package com.foryoung.foryoung.review.repository;

import com.foryoung.foryoung.review.dto.PerformanceReviewResponse;

import java.util.List;

public interface PerformanceReviewRepositoryCustom {


    List<PerformanceReviewResponse> findReviews(Long memberId);


    List<PerformanceReviewResponse> findMyReviewsWithCount(Long memberId);


    List<PerformanceReviewResponse> findReviewsByPerformance(
            Long performanceId,
            Long memberId
    );


}