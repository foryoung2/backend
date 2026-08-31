package com.foryoung.foryoung.review.repository;

import com.foryoung.foryoung.review.entity.ReviewLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {


    boolean existsByMember_IdAndPerformanceReview_Id(
            Long memberId,
            Long reviewId
    );


    Optional<ReviewLike> findByMember_IdAndPerformanceReview_Id(
            Long memberId,
            Long reviewId
    );


    long countByPerformanceReview_Id(Long reviewId);


    @Query(
            value = """
                select rl
                from ReviewLike rl
                join fetch rl.performanceReview review
                join fetch review.performanceRecord record
                join fetch record.member member
                join fetch record.schedule schedule
                join fetch schedule.performance performance
                where rl.member.id = :memberId
                """,
            countQuery = """
                select count(rl)
                from ReviewLike rl
                where rl.member.id = :memberId
                """
    )
    Page<ReviewLike> findLikedReviews(
            @Param("memberId") Long memberId,
            Pageable pageable
    );


    @Query("""
        select rl.performanceReview.id
        from ReviewLike rl
        where rl.member.id = :memberId
    """)
    Set<Long> findLikedReviewIds(@Param("memberId") Long memberId);


}