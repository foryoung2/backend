package com.foryoung.foryoung.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewLikeResponse {


    private boolean liked;

    private long likeCount;


}