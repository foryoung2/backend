package com.foryoung.foryoung.review.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCommentResponse {


    private Long id;

    private Long parentCommentId;

    private String writerNickname;

    private String content;

    private LocalDateTime createdAt;

    @Builder.Default
    private List<ReviewCommentResponse> replies = new ArrayList<>();

    public void initializeReplies() {
        this.replies = new ArrayList<>();
    }

    public void addReply(ReviewCommentResponse reply) {
        replies.add(reply);
    }


}