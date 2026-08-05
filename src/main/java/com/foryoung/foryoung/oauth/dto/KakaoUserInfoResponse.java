package com.foryoung.foryoung.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoResponse {


    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;


    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {

        private String email;

    }


}