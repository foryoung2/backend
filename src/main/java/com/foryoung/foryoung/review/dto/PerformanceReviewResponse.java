package com.foryoung.foryoung.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PerformanceReviewResponse {


    private Long id;

    private Long performanceRecordId;

    private String writerNickname;

    private String title;

    private String content;

    private boolean publicReview;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private long likeCount;

    private long commentCount;

    private boolean liked;


    public void setLiked(boolean liked) {
        this.liked = liked;
    }


    public void updateCounts(Long likeCount,
                             Long commentCount) {

        this.likeCount = likeCount;
        this.commentCount = commentCount;

    }


}