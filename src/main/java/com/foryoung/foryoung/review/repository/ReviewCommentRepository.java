package com.foryoung.foryoung.review.repository;

import com.foryoung.foryoung.review.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {


    Optional<ReviewComment> findByIdAndMember_Id(
            Long commentId,
            Long memberId
    );


    long countByPerformanceReview_IdAndDeletedFalse(Long reviewId);


    @Query("""
    select c
    from ReviewComment c
    join fetch c.member
    left join fetch c.parentComment
    where c.performanceReview.id = :reviewId
    order by c.createdAt asc
    """)
    List<ReviewComment> findByReviewIdWithMemberAndParent(
            @Param("reviewId") Long reviewId
    );


}