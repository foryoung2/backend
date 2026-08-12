package com.foryoung.foryoung.review.mapper;

import com.foryoung.foryoung.review.dto.ReviewCommentResponse;
import com.foryoung.foryoung.review.entity.ReviewComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewCommentMapper {


    @Mapping(target = "parentCommentId", source = "parentComment.id")
    @Mapping(target = "writerNickname",
            expression = "java(comment.getMember().getDisplayNickname())"
    )
    @Mapping(
            target = "content",
            expression = "java(comment.isDeleted() ? \"삭제된 댓글입니다.\" : comment.getContent())"
    )
    @Mapping(target = "replies", ignore = true)
    ReviewCommentResponse toReviewCommentResponse(ReviewComment comment);


}