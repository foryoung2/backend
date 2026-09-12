package com.foryoung.foryoung.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateRequest {


    @NotBlank(message = "Content must not be blank")
    private String content;


}