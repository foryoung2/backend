package com.foryoung.foryoung.review.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.member.entity.Member;
import com.foryoung.foryoung.member.service.MemberService;
import com.foryoung.foryoung.review.dto.LikedReviewResponse;
import com.foryoung.foryoung.review.dto.ReviewLikeResponse;
import com.foryoung.foryoung.review.entity.PerformanceReview;
import com.foryoung.foryoung.review.entity.ReviewLike;
import com.foryoung.foryoung.review.mapper.ReviewLikeMapper;
import com.foryoung.foryoung.review.repository.PerformanceReviewRepository;
import com.foryoung.foryoung.review.repository.ReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewLikeService {

    private final PerformanceReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    private final MemberService memberService;

    private final ReviewLikeMapper reviewLikeMapper;


    @Transactional
    public ReviewLikeResponse likeReview(Long memberId,
                                         Long reviewId) {

        PerformanceReview review = reviewRepository.findAccessibleReview(reviewId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_REVIEW_NOT_FOUND));

        if (reviewLikeRepository
                .existsByMember_IdAndPerformanceReview_Id(memberId, reviewId)) {

            throw new CustomException(ErrorCode.REVIEW_LIKE_ALREADY_EXISTS);
        }
        Member member = memberService.findMemberById(memberId);

        ReviewLike reviewLike = ReviewLike.builder()
                .member(member)
                .performanceReview(review)
                .build();

        reviewLikeRepository.save(reviewLike);

        return ReviewLikeResponse.builder()
                .liked(true)
                .likeCount(reviewLikeRepository.countByPerformanceReview_Id(reviewId))
                .build();

    }

    @Transactional
    public ReviewLikeResponse unlikeReview(Long memberId,
                                           Long reviewId) {

        ReviewLike reviewLike =
                reviewLikeRepository.findByMember_IdAndPerformanceReview_Id(memberId, reviewId)
                        .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_LIKE_NOT_FOUND));

        reviewLikeRepository.delete(reviewLike);

        return ReviewLikeResponse.builder()
                .liked(false)
                .likeCount(reviewLikeRepository.countByPerformanceReview_Id(reviewId))
                .build();

    }

    public List<LikedReviewResponse> getLikedReviews(Long memberId) {

        return reviewLikeRepository
                .findLikedReviews(memberId)
                .stream()
                .map(reviewLikeMapper::toLikedReviewResponse)
                .toList();

    }


}