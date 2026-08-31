package com.foryoung.foryoung.review.repository;

import com.foryoung.foryoung.member.entity.MemberStatus;
import com.foryoung.foryoung.performance.entity.QPerformance;
import com.foryoung.foryoung.performance.entity.QPerformanceRecord;
import com.foryoung.foryoung.performance.entity.QPerformanceSchedule;
import com.foryoung.foryoung.review.dto.PerformanceReviewResponse;
import com.foryoung.foryoung.review.entity.QPerformanceReview;
import com.foryoung.foryoung.review.entity.QReviewComment;
import com.foryoung.foryoung.review.entity.QReviewLike;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PerformanceReviewRepositoryImpl implements PerformanceReviewRepositoryCustom {


    private final JPAQueryFactory queryFactory;


    @Override
    public Page<PerformanceReviewResponse> findReviews(Long memberId,
                                                       Pageable pageable) {

        QPerformanceReview review = QPerformanceReview.performanceReview;

        QReviewLike like = QReviewLike.reviewLike;

        QReviewComment comment = QReviewComment.reviewComment;

        BooleanExpression accessibleCondition = review.publicReview.isTrue();

        if (memberId != null) {
            accessibleCondition = accessibleCondition.or(
                    review.performanceRecord.member.id.eq(memberId)
            );
        }

        Expression<Boolean> ownerExpression;

        if (memberId != null) {
            ownerExpression = review.performanceRecord.member.id.eq(memberId);
        } else {
            ownerExpression = Expressions.constant(false);
        }

        List<PerformanceReviewResponse> content =
                queryFactory
                        .select(
                                Projections.constructor(
                                        PerformanceReviewResponse.class,
                                        review.id,
                                        review.performanceRecord.id,
                                        review.performanceRecord.member.status
                                                .when(MemberStatus.DELETED)
                                                .then("탈퇴한 사용자")
                                                .otherwise(review.performanceRecord.member.nickname),
                                        review.title,
                                        review.content,
                                        review.publicReview,
                                        review.createdAt,
                                        review.updatedAt,
                                        like.id.countDistinct(),
                                        comment.id.countDistinct(),
                                        Expressions.constant(false),
                                        ownerExpression
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

                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())

                        .fetch();

        Long total = queryFactory
                .select(review.count())
                .from(review)
                .where(accessibleCondition)
                .fetchOne();

        return new PageImpl<>(
                content,
                pageable,
                total != null ? total : 0L
        );

    }


    @Override
    public Page<PerformanceReviewResponse> findMyReviewsWithCount(Long memberId,
                                                                  Pageable pageable) {

        QPerformanceReview review = QPerformanceReview.performanceReview;

        QReviewLike like = QReviewLike.reviewLike;

        QReviewComment comment = QReviewComment.reviewComment;

        Expression<Boolean> ownerExpression = review.performanceRecord.member.id.eq(memberId);

        List<PerformanceReviewResponse> content =
                queryFactory
                        .select(
                                Projections.constructor(
                                        PerformanceReviewResponse.class,
                                        review.id,
                                        review.performanceRecord.id,
                                        review.performanceRecord.member.status
                                                .when(MemberStatus.DELETED)
                                                .then("탈퇴한 사용자")
                                                .otherwise(review.performanceRecord.member.nickname),
                                        review.title,
                                        review.content,
                                        review.publicReview,
                                        review.createdAt,
                                        review.updatedAt,
                                        like.id.countDistinct(),
                                        comment.id.countDistinct(),
                                        Expressions.constant(false),
                                        ownerExpression
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

                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())

                        .fetch();

        Long total =
                queryFactory
                        .select(review.count())
                        .from(review)
                        .where(review.performanceRecord.member.id.eq(memberId))
                        .fetchOne();


        return new PageImpl<>(
                content,
                pageable,
                total != null ? total : 0L
        );

    }


    @Override
    public Page<PerformanceReviewResponse> findReviewsByPerformance(Long performanceId,
                                                                    Long memberId,
                                                                    Pageable pageable) {

        QPerformanceReview review = QPerformanceReview.performanceReview;

        QPerformanceRecord performanceRecord = QPerformanceRecord.performanceRecord;

        QPerformanceSchedule schedule = QPerformanceSchedule.performanceSchedule;

        QPerformance performance = QPerformance.performance;

        QReviewLike like = QReviewLike.reviewLike;

        QReviewComment comment = QReviewComment.reviewComment;

        BooleanExpression accessibleCondition = review.publicReview.isTrue();

        if (memberId != null) {
            accessibleCondition = accessibleCondition.or(
                    review.performanceRecord.member.id.eq(memberId)
            );
        }

        Expression<Boolean> ownerExpression;

        if (memberId != null) {
            ownerExpression = review.performanceRecord.member.id.eq(memberId);
        } else {
            ownerExpression = Expressions.constant(false);
        }

        List<PerformanceReviewResponse> content =
                queryFactory
                        .select(
                                Projections.constructor(
                                        PerformanceReviewResponse.class,
                                        review.id,
                                        review.performanceRecord.id,
                                        review.performanceRecord.member.status
                                                .when(MemberStatus.DELETED)
                                                .then("탈퇴한 사용자")
                                                .otherwise(
                                                       review.performanceRecord.member.nickname),
                                        review.title,
                                        review.content,
                                        review.publicReview,
                                        review.createdAt,
                                        review.updatedAt,
                                        like.id.countDistinct(),
                                        comment.id.countDistinct(),
                                        Expressions.constant(false),
                                        ownerExpression
                                )
                        )
                        .from(review)

                        .join(review.performanceRecord, performanceRecord)

                        .join(performanceRecord.schedule, schedule)

                        .join(schedule.performance, performance)

                        .leftJoin(like)
                        .on(like.performanceReview.eq(review))

                        .leftJoin(comment)
                        .on(
                                comment.performanceReview.eq(review)
                                        .and(comment.deleted.isFalse())
                        )

                        .where(
                                performance.id.eq(performanceId),
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

                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())

                        .fetch();

        Long total = queryFactory
                .select(review.count())
                .from(review)

                .join(review.performanceRecord, performanceRecord)
                .join(performanceRecord.schedule, schedule)
                .join(schedule.performance, performance)

                .where(
                        performance.id.eq(performanceId),
                        accessibleCondition
                )

                .fetchOne();

        return new PageImpl<>(
                content,
                pageable,
                total != null ? total : 0L
        );

    }


}