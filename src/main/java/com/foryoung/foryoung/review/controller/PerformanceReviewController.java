package com.foryoung.foryoung.review.controller;

import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.review.dto.PerformanceReviewCreateRequest;
import com.foryoung.foryoung.review.dto.PerformanceReviewResponse;
import com.foryoung.foryoung.review.dto.PerformanceReviewUpdateRequest;
import com.foryoung.foryoung.review.service.PerformanceReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PerformanceReviewController {


    private final PerformanceReviewService reviewService;


    @PostMapping("/performance-reviews")
    public ResponseEntity<PerformanceReviewResponse> createReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @Valid @RequestBody PerformanceReviewCreateRequest request) {

        PerformanceReviewResponse response = reviewService.createReview(userDetails.getMemberId(), request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


    @PatchMapping("/performance-reviews/{reviewId}")
    public ResponseEntity<PerformanceReviewResponse> updateReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @PathVariable Long reviewId,
                                                                  @RequestBody PerformanceReviewUpdateRequest request) {

        return ResponseEntity.ok(reviewService.updateReview(userDetails.getMemberId(), reviewId, request));

    }


    @GetMapping("/performance-reviews/{reviewId}")
    public ResponseEntity<PerformanceReviewResponse> getReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable Long reviewId) {

        Long memberId = userDetails != null ? userDetails.getMemberId() : null;

        return ResponseEntity.ok(reviewService.getReview(reviewId, memberId));

    }


    @GetMapping("/performance-reviews")
    public ResponseEntity<List<PerformanceReviewResponse>> getReviews(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails != null ? userDetails.getMemberId() : null;

        return ResponseEntity.ok(reviewService.getReviews(memberId));

    }


    @GetMapping("/performance-reviews/me")
    public ResponseEntity<List<PerformanceReviewResponse>> getMyReviews(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(reviewService.getMyReviews(userDetails.getMemberId()));

    }


    @GetMapping("/performances/{performanceId}/reviews")
    public ResponseEntity<List<PerformanceReviewResponse>> getReviewsByPerformance(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                   @PathVariable Long performanceId) {

        Long memberId = userDetails != null ? userDetails.getMemberId() : null;

        return ResponseEntity.ok(reviewService.getReviewsByPerformance(performanceId, memberId));

    }


    @DeleteMapping("/performance-reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Long reviewId) {

        reviewService.deleteReview(userDetails.getMemberId(), reviewId);

        return ResponseEntity.noContent().build();

    }


}