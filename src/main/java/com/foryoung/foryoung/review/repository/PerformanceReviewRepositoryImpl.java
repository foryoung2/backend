package com.foryoung.foryoung.review.repository;

import com.foryoung.foryoung.member.entity.MemberStatus;
import com.foryoung.foryoung.review.entity.QPerformanceReview;
import com.foryoung.foryoung.review.entity.QReviewComment;
import com.foryoung.foryoung.review.entity.QReviewLike;
import com.foryoung.foryoung.review.dto.PerformanceReviewResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PerformanceReviewRepositoryImpl implements PerformanceReviewRepositoryCustom {


    private final JPAQueryFactory queryFactory;


    @Override
    public List<PerformanceReviewResponse> findReviews(Long memberId) {

        QPerformanceReview review = QPerformanceReview.performanceReview;

        QReviewLike like = QReviewLike.reviewLike;

        QReviewComment comment = QReviewComment.reviewComment;

        BooleanExpression accessibleCondition = review.publicReview.isTrue();

        if (memberId != null) {
            accessibleCondition = accessibleCondition.or(
                    review.performanceRecord.member.id.eq(memberId)
            );
        }

        return queryFactory
                .select(
                        Projections.constructor(
                                PerformanceReviewResponse.class,
                                review.id,
                                review.performanceRecord.id,
                                review.performanceRecord.member.status
                                        .when(MemberStatus.DELETED)
                                        .then("탈퇴한 사용자")
                                        .otherwise(
                                                review.performanceRecord.member.nickname
                                        ),
                                review.title,
                                review.content,
                                review.publicReview,
                                review.createdAt,
                                review.updatedAt,
                                like.id.countDistinct(),
                                comment.id.countDistinct(),
                                Expressions.constant(false)
                        )
                )
                .from(review)

                .leftJoin(like)
                .on(like.performanceReview.eq(review))

                .leftJoin(comment)
                .on(
                        comment.performanceReview.eq(review)
                                .and(comment.deleted.isFalse())
                )

                .where(accessibleCondition)

                .groupBy(
                        review.id,
                        review.performanceRecord.id,
                        review.performanceRecord.member.status,
                        review.performanceRecord.member.nickname,
                        review.title,
                        review.content,
                        review.publicReview,
                        review.createdAt,
                        review.updatedAt
                )

                .orderBy(review.createdAt.desc())

                .fetch();

    }


    @Override
    public List<PerformanceReviewResponse> findMyReviewsWithCount(Long memberId) {

        QPerformanceReview review = QPerformanceReview.performanceReview;

        QReviewLike like = QReviewLike.reviewLike;

        QReviewComment comment = QReviewComment.reviewComment;

        return queryFactory
                .select(
                        Projections.constructor(
                                PerformanceReviewResponse.class,
                                review.id,
                                review.performanceRecord.id,
                                review.performanceRecord.member.status
                                        .when(MemberStatus.DELETED)
                                        .then("탈퇴한 사용자")
                                        .otherwise(
                                                review.performanceRecord.member.nickname
                                        ),
                                review.title,
                                review.content,
                                review.publicReview,
                                review.createdAt,
                                review.updatedAt,
                                like.id.countDistinct(),
                                comment.id.countDistinct(),
                                Expressions.constant(false)
                        )
                )
                .from(review)

                .leftJoin(like)
                .on(like.performanceReview.eq(review))

                .leftJoin(comment)
                .on(
                        comment.performanceReview.eq(review)
                                .and(comment.deleted.isFalse())
                )

                .where(review.performanceRecord.member.id.eq(memberId))

                .groupBy(
                        review.id,
                        review.performanceRecord.id,
                        review.performanceRecord.member.status,
                        review.performanceRecord.member.nickname,
                        review.title,
                        review.content,
                        review.publicReview,
                        review.createdAt,
                        review.updatedAt
                )

                .orderBy(review.createdAt.desc())

                .fetch();

    }


    @Override
    public List<PerformanceReviewResponse> findReviewsByPerformance(
            Long performanceId,
            Long memberId) {

        QPerformanceReview review = QPerformanceReview.performanceReview;

        QReviewLike like = QReviewLike.reviewLike;

        QReviewComment comment = QReviewComment.reviewComment;

        BooleanExpression accessibleCondition = review.publicReview.isTrue();

        if (memberId != null) {
            accessibleCondition = accessibleCondition.or(
                    review.performanceRecord.member.id.eq(memberId)
            );
        }

        return queryFactory
                .select(
                        Projections.constructor(
                                PerformanceReviewResponse.class,
                                review.id,
                                review.performanceRecord.id,
                                review.performanceRecord.member.status
                                        .when(MemberStatus.DELETED)
                                        .then("탈퇴한 사용자")
                                        .otherwise(
                                                review.performanceRecord.member.nickname
                                        ),
                                review.title,
                                review.content,
                                review.publicReview,
                                review.createdAt,
                                review.updatedAt,
                                like.id.countDistinct(),
                                comment.id.countDistinct(),
                                Expressions.constant(false)
                        )
                )
                .from(review)

                .leftJoin(like)
                .on(like.performanceReview.eq(review))

                .leftJoin(comment)
                .on(
                        comment.performanceReview.eq(review)
                                .and(comment.deleted.isFalse())
                )

                .where(
                        review.performanceRecord.schedule.performance.id
                                .eq(performanceId),
                        accessibleCondition
                )

                .groupBy(
                        review.id,
                        review.performanceRecord.id,
                        review.performanceRecord.member.status,
                        review.performanceRecord.member.nickname,
                        review.title,
                        review.content,
                        review.publicReview,
                        review.createdAt,
                        review.updatedAt
                )

                .orderBy(review.createdAt.desc())

                .fetch();

    }


}