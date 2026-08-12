package com.foryoung.foryoung.review.repository;

import com.foryoung.foryoung.review.entity.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long>, PerformanceReviewRepositoryCustom {


    Optional<PerformanceReview> findByIdAndPerformanceRecord_Member_Id(Long reviewId, Long memberId);


    @Query("""
            select r
            from PerformanceReview r
            join r.performanceRecord pr
            where r.id = :reviewId
              and (
                    r.publicReview = true
                    or pr.member.id = :memberId
                  )
            """)
    Optional<PerformanceReview> findAccessibleReview(
            @Param("reviewId") Long reviewId,
            @Param("memberId") Long memberId
    );


    @Query("""
            select r
            from PerformanceReview r
            where r.id = :reviewId
              and r.publicReview = true
            """)
    Optional<PerformanceReview> findPublicReview(
            @Param("reviewId") Long reviewId
    );


}