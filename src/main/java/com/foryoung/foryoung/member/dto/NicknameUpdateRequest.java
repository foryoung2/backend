package com.foryoung.foryoung.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NicknameUpdateRequest {


    @NotBlank(message = "Nickname must not be blank")
    @Size(max = 20, message = "Nickname must not exceed 20 characters")
    private String nickname;


}