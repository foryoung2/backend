package com.foryoung.foryoung.review.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.performance.entity.PerformanceRecord;
import com.foryoung.foryoung.performance.repository.PerformanceRecordRepository;
import com.foryoung.foryoung.review.dto.PerformanceReviewCreateRequest;
import com.foryoung.foryoung.review.dto.PerformanceReviewResponse;
import com.foryoung.foryoung.review.dto.PerformanceReviewUpdateRequest;
import com.foryoung.foryoung.review.entity.PerformanceReview;
import com.foryoung.foryoung.review.mapper.PerformanceReviewMapper;
import com.foryoung.foryoung.review.repository.PerformanceReviewRepository;
import com.foryoung.foryoung.review.repository.ReviewCommentRepository;
import com.foryoung.foryoung.review.repository.ReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceReviewService {

    private final PerformanceReviewRepository reviewRepository;
    private final PerformanceRecordRepository recordRepository;

    private final ReviewLikeRepository likeRepository;
    private final ReviewCommentRepository commentRepository;

    private final PerformanceReviewMapper reviewMapper;


    @Transactional
    public PerformanceReviewResponse createReview(Long memberId,
                                                  PerformanceReviewCreateRequest request) {

        PerformanceRecord record =
                recordRepository.findByIdAndMember_Id(request.getPerformanceRecordId(), memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_RECORD_NOT_FOUND));

        PerformanceReview review = PerformanceReview.builder()
                .performanceRecord(record)
                .title(request.getTitle())
                .content(request.getContent())
                .publicReview(request.isPublicReview())
                .build();

        PerformanceReview savedReview = reviewRepository.save(review);

        return toReviewResponse(savedReview, memberId);

    }


    @Transactional
    public PerformanceReviewResponse updateReview(Long memberId,
                                                  Long reviewId,
                                                  PerformanceReviewUpdateRequest request) {

        PerformanceReview review =
                reviewRepository.findByIdAndPerformanceRecord_Member_Id(reviewId, memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_REVIEW_NOT_FOUND));

        review.updateReview(
                request.getTitle(),
                request.getContent(),
                request.getPublicReview()
        );

        return toReviewResponse(review, memberId);

    }


    public PerformanceReviewResponse getReview(Long reviewId,
                                               Long memberId) {

        PerformanceReview review;

        if (memberId == null) {

            review = reviewRepository.findPublicReview(reviewId)
                    .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_REVIEW_NOT_FOUND));

            PerformanceReviewResponse response = toReviewResponse(review);
            response.setOwner(false);

            return response;

        }

        review = reviewRepository.findAccessibleReview(reviewId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_REVIEW_NOT_FOUND));

        return toReviewResponse(review, memberId);

    }


    public Page<PerformanceReviewResponse> getReviews(Long memberId,
                                                      Pageable pageable) {

        Page<PerformanceReviewResponse> reviews = reviewRepository.findReviews(memberId, pageable);
        applyLiked(reviews.getContent(), memberId);

        return reviews;

    }


    public Page<PerformanceReviewResponse> getMyReviews(Long memberId,
                                                        Pageable pageable) {

        Page<PerformanceReviewResponse> reviews = reviewRepository.findMyReviewsWithCount(memberId, pageable);

        applyLiked(reviews.getContent(), memberId);

        return reviews;
    }


    public Page<PerformanceReviewResponse> getReviewsByPerformance(Long performanceId,
                                                                   Long memberId,
                                                                   Pageable pageable) {

        Page<PerformanceReviewResponse> reviews = reviewRepository.findReviewsByPerformance(performanceId, memberId, pageable);
        applyLiked(reviews.getContent(), memberId);

        return reviews;

    }


    @Transactional
    public void deleteReview(Long memberId,
                             Long reviewId) {

        PerformanceReview review = reviewRepository.findByIdAndPerformanceRecord_Member_Id(reviewId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_REVIEW_NOT_FOUND));

        reviewRepository.delete(review);

    }


    private PerformanceReviewResponse toReviewResponse(PerformanceReview review) {

        PerformanceReviewResponse response = reviewMapper.toPerformanceReviewResponse(review);

        response.updateCounts(likeRepository.countByPerformanceReview_Id(review.getId()),
                commentRepository.countByPerformanceReview_IdAndDeletedFalse(review.getId()));

        response.setLiked(false);

        return response;

    }


    private PerformanceReviewResponse toReviewResponse(PerformanceReview review,
                                                       Long memberId) {

        PerformanceReviewResponse response = toReviewResponse(review);

        response.setOwner(
                review.getPerformanceRecord()
                        .getMember()
                        .getId()
                        .equals(memberId)
        );

        response.setLiked(likeRepository.existsByMember_IdAndPerformanceReview_Id(memberId, review.getId()));

        return response;

    }


    private void applyLiked(List<PerformanceReviewResponse> reviews,
                            Long memberId) {

        if (reviews.isEmpty()) {
            return;
        }

        Set<Long> likedReviewIds = likeRepository.findLikedReviewIds(memberId);

        reviews.forEach(review ->
                review.setLiked(likedReviewIds.contains(review.getId()))
        );

    }


}