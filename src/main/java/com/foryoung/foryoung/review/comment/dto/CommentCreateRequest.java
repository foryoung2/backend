package com.foryoung.foryoung.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {


    @NotBlank(message = "Content must not be blank")
    private String content;


}