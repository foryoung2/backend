package com.foryoung.foryoung.review.controller;

import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.review.dto.LikedReviewResponse;
import com.foryoung.foryoung.review.dto.ReviewLikeResponse;
import com.foryoung.foryoung.review.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<LikedReviewResponse>> getLikedReviews(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(reviewLikeService.getLikedReviews(userDetails.getMemberId()));

    }


}