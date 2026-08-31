package com.foryoung.foryoung.review.repository;

import com.foryoung.foryoung.review.dto.PerformanceReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerformanceReviewRepositoryCustom {


    Page<PerformanceReviewResponse> findReviews(
            Long memberId,
            Pageable pageable
    );


    Page<PerformanceReviewResponse> findMyReviewsWithCount(
            Long memberId,
            Pageable pageable
    );


    Page<PerformanceReviewResponse> findReviewsByPerformance(
            Long performanceId,
            Long memberId,
            Pageable pageable
    );


}