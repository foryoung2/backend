package com.foryoung.foryoung.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PerformanceReviewCreateRequest {


    @NotNull(message = "Performance record id must not be null")
    private Long performanceRecordId;

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Content must not be blank")
    private String content;

    private boolean publicReview;


}