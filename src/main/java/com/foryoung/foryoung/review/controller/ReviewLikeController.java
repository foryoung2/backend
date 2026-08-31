package com.foryoung.foryoung.review.controller;

import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.global.pagination.PageResponse;
import com.foryoung.foryoung.review.dto.LikedReviewResponse;
import com.foryoung.foryoung.review.dto.ReviewLikeResponse;
import com.foryoung.foryoung.review.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performance-reviews")
public class ReviewLikeController {


    private final ReviewLikeService reviewLikeService;

    
    @PostMapping("/{reviewId}/likes")
    public ResponseEntity<ReviewLikeResponse> likeReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @PathVariable Long reviewId) {

        return ResponseEntity.ok(reviewLikeService.likeReview(userDetails.getMemberId(), reviewId));

    }


    @DeleteMapping("/{reviewId}/likes")
    public ResponseEntity<ReviewLikeResponse> unlikeReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @PathVariable Long reviewId) {

        return ResponseEntity.ok(reviewLikeService.unlikeReview(userDetails.getMemberId(), reviewId));

    }


    @GetMapping("/liked")
    public ResponseEntity<PageResponse<LikedReviewResponse>> getLikedReviews(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                             @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
                                                                             Pageable pageable) {

        Page<LikedReviewResponse> response =
                reviewLikeService.getLikedReviews(userDetails.getMemberId(), pageable);

        return ResponseEntity.ok(PageResponse.from(response));

    }


}