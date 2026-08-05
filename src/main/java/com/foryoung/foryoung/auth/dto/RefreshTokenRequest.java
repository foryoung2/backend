package com.foryoung.foryoung.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {


    @NotBlank(message = "Refresh token must not be blank")
    private String refreshToken;


}