package com.foryoung.foryoung.review.controller;

import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.review.dto.CommentCountResponse;
import com.foryoung.foryoung.review.dto.CommentCreateRequest;
import com.foryoung.foryoung.review.dto.CommentUpdateRequest;
import com.foryoung.foryoung.review.dto.ReviewCommentResponse;
import com.foryoung.foryoung.review.service.ReviewCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performance-reviews/comments")
public class ReviewCommentController {


    private final ReviewCommentService commentService;


    @PostMapping("/{reviewId}")
    public ResponseEntity<ReviewCommentResponse> createComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable Long reviewId,
                                                               @Valid @RequestBody CommentCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createComment(userDetails.getMemberId(), reviewId, request));

    }


    @PostMapping("/{commentId}/replies")
    public ResponseEntity<ReviewCommentResponse> createReply(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable Long commentId,
                                                             @Valid @RequestBody CommentCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createReply(userDetails.getMemberId(), commentId, request));

    }


    @PatchMapping("/{commentId}")
    public ResponseEntity<ReviewCommentResponse> updateComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable Long commentId,
                                                               @Valid @RequestBody CommentUpdateRequest request) {

        return ResponseEntity.ok(commentService.updateComment(userDetails.getMemberId(), commentId, request));

    }


    @GetMapping("/{reviewId}")
    public ResponseEntity<List<ReviewCommentResponse>> getComments(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @PathVariable Long reviewId) {

        Long memberId = userDetails != null
                ? userDetails.getMemberId()
                : null;
        return ResponseEntity.ok(commentService.getComments(reviewId, memberId));

    }


    @GetMapping("/{reviewId}/count")
    public ResponseEntity<CommentCountResponse> getCommentCount(@PathVariable Long reviewId) {

        return ResponseEntity.ok(commentService.getCommentCount(reviewId));

    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable Long commentId) {

        commentService.deleteComment(userDetails.getMemberId(), commentId);

        return ResponseEntity.noContent().build();

    }


}