package com.foryoung.foryoung.auth.service;

import com.foryoung.foryoung.auth.dto.JwtTokenResponse;
import com.foryoung.foryoung.global.jwt.JwtTokenProvider;
import com.foryoung.foryoung.member.entity.Member;
import com.foryoung.foryoung.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {


    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;


    public void logout(String accessToken) {

        String email = jwtTokenProvider.getUsername(accessToken);

        tokenService.deleteRefreshToken(email);
        tokenService.saveLogoutToken(accessToken);
    }


    public JwtTokenResponse refresh(String refreshToken) {

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token is invalid");
        }

        String email = jwtTokenProvider.getUsername(refreshToken);

        String savedToken = tokenService.getRefreshToken(email);

        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token does not match the stored token");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email));

        JwtTokenResponse tokenResponse = jwtTokenProvider.generateToken(member);

        tokenService.saveRefreshToken(member, tokenResponse);

        return tokenResponse;
    }


}