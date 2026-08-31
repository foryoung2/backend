package com.foryoung.foryoung.review.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.member.entity.Member;
import com.foryoung.foryoung.member.service.MemberService;
import com.foryoung.foryoung.review.dto.CommentCountResponse;
import com.foryoung.foryoung.review.dto.CommentCreateRequest;
import com.foryoung.foryoung.review.dto.CommentUpdateRequest;
import com.foryoung.foryoung.review.dto.ReviewCommentResponse;
import com.foryoung.foryoung.review.entity.PerformanceReview;
import com.foryoung.foryoung.review.entity.ReviewComment;
import com.foryoung.foryoung.review.mapper.ReviewCommentMapper;
import com.foryoung.foryoung.review.repository.PerformanceReviewRepository;
import com.foryoung.foryoung.review.repository.ReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommentService {


    private final PerformanceReviewRepository reviewRepository;
    private final ReviewCommentRepository commentRepository;

    private final MemberService memberService;

    private final ReviewCommentMapper commentMapper;


    @Transactional
    public ReviewCommentResponse createComment(Long memberId,
                                               Long reviewId,
                                               CommentCreateRequest request) {

        PerformanceReview review = reviewRepository.findAccessibleReview(reviewId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_REVIEW_NOT_FOUND));

        Member member = memberService.findMemberById(memberId);

        ReviewComment comment = ReviewComment.builder()
                .performanceReview(review)
                .member(member)
                .content(request.getContent())
                .build();

        ReviewComment savedComment = commentRepository.save(comment);

        return commentMapper.toReviewCommentResponse(savedComment, memberId);

    }


    @Transactional
    public ReviewCommentResponse createReply(Long memberId,
                                             Long parentCommentId,
                                             CommentCreateRequest request) {

        ReviewComment parent = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (parent.isReply()) {
            throw new CustomException(ErrorCode.REPLY_CANNOT_HAVE_REPLY);
        }

        Long reviewId = parent.getPerformanceReview().getId();

        PerformanceReview review = reviewRepository.findAccessibleReview(reviewId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_REVIEW_NOT_FOUND));

        Member member = memberService.findMemberById(memberId);

        ReviewComment reply = ReviewComment.builder()
                .performanceReview(review)
                .member(member)
                .parentComment(parent)
                .content(request.getContent())
                .build();

        ReviewComment savedReply = commentRepository.save(reply);

        return commentMapper.toReviewCommentResponse(savedReply, memberId);

    }


    @Transactional
    public ReviewCommentResponse updateComment(Long memberId,
                                               Long commentId,
                                               CommentUpdateRequest request) {

        ReviewComment comment = commentRepository.findByIdAndMember_Id(commentId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        comment.updateComment(request.getContent());

        return commentMapper.toReviewCommentResponse(comment, memberId);

    }


    public List<ReviewCommentResponse> getComments(Long reviewId,
                                                   Long memberId) {

        List<ReviewComment> comments =
                commentRepository.findByReviewIdWithMemberAndParent(reviewId);

        return buildCommentTree(comments, memberId);

    }


    public CommentCountResponse getCommentCount(Long reviewId) {

        long count = commentRepository.countByPerformanceReview_IdAndDeletedFalse(reviewId);

        return CommentCountResponse.builder()
                .commentCount(count)
                .build();

    }


    @Transactional
    public void deleteComment(Long memberId,
                              Long commentId) {

        ReviewComment comment = commentRepository.findByIdAndMember_Id(commentId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if(comment.isDeleted()) {
            throw new CustomException(ErrorCode.COMMENT_ALREADY_DELETED);
        }

        comment.deleteComment();

    }


    private List<ReviewCommentResponse> buildCommentTree(List<ReviewComment> comments,
                                                         Long memberId) {

        Map<Long,ReviewCommentResponse> responseMap = new HashMap<>();
        List<ReviewCommentResponse> roots = new ArrayList<>();

        for (ReviewComment comment : comments) {

            ReviewCommentResponse response = commentMapper.toReviewCommentResponse(comment, memberId);
            response.initializeReplies();

            responseMap.put(comment.getId(), response);
        }

        for(ReviewComment comment : comments) {

            ReviewCommentResponse current = responseMap.get(comment.getId());

            if(comment.getParentComment() == null) {
                roots.add(current);

                continue;
            }

            ReviewCommentResponse parent = responseMap.get(comment.getParentComment().getId());

            if (parent != null) {
                parent.addReply(current);
            }
        }

        return roots;

    }


}