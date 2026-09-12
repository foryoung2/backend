package com.foryoung.foryoung.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LikedReviewResponse {


    private Long reviewId;

    private String reviewTitle;

    private String performanceTitle;

    private String writerNickname;

    private LocalDateTime likedAt;


}