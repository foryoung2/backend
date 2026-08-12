package com.foryoung.foryoung.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PerformanceReviewUpdateRequest {


    private String title;

    private String content;

    private Boolean publicReview;


}