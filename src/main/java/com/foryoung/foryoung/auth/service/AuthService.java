package com.foryoung.foryoung.auth.service;

import com.foryoung.foryoung.auth.dto.JwtTokenResponse;
import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.global.jwt.JwtTokenProvider;
import com.foryoung.foryoung.member.entity.Member;
import com.foryoung.foryoung.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtTokenProvider.getUsername(refreshToken);

        String savedToken = tokenService.getRefreshToken(email);

        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        JwtTokenResponse tokenResponse = jwtTokenProvider.generateToken(member);

        tokenService.saveRefreshToken(member, tokenResponse);

        return tokenResponse;

    }


}