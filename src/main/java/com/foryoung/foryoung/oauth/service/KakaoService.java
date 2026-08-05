package com.foryoung.foryoung.oauth.service;

import com.foryoung.foryoung.oauth.properties.KakaoProperties;
import com.foryoung.foryoung.oauth.dto.KakaoTokenResponse;
import com.foryoung.foryoung.oauth.dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoService {


    private final RestTemplate restTemplate;
    private final KakaoProperties properties;


    public KakaoTokenResponse requestKakaoAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add("grant_type", "authorization_code");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getClientSecret());
        form.add("redirect_uri", properties.getRedirectUri());
        form.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        ResponseEntity<KakaoTokenResponse> response =
                restTemplate.exchange(
                        properties.getTokenUri(),
                        HttpMethod.POST,
                        entity,
                        KakaoTokenResponse.class
                );

        return response.getBody();
    }


    public String requestUserEmail(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoResponse> response =
                restTemplate.exchange(
                        properties.getUserInfoUri(),
                        HttpMethod.GET,
                        entity,
                        KakaoUserInfoResponse.class
                );

        return response.getBody()
                .getKakaoAccount()
                .getEmail();
    }


}