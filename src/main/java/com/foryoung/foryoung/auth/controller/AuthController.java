package com.foryoung.foryoung.auth.controller;

import com.foryoung.foryoung.auth.dto.JwtTokenResponse;
import com.foryoung.foryoung.auth.dto.RefreshTokenRequest;
import com.foryoung.foryoung.auth.service.AuthService;
import com.foryoung.foryoung.auth.util.TokenUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String accessToken = TokenUtils.resolveToken(authorizationHeader);

        if (accessToken == null) {
            return ResponseEntity.badRequest().build();
        }

        authService.logout(accessToken);

        return ResponseEntity.noContent().build();

    }


    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));

    }


}