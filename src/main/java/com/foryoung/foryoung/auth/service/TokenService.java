package com.foryoung.foryoung.auth.service;

import com.foryoung.foryoung.auth.dto.JwtTokenResponse;
import com.foryoung.foryoung.global.jwt.JwtTokenProvider;
import com.foryoung.foryoung.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {


    private static final String REFRESH_PREFIX = "RT:";
    private static final String BLACKLIST_PREFIX = "BL:";
    private static final String LOGOUT = "logout";

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;


    public void saveRefreshToken(Member member,
                                 JwtTokenResponse tokenDto) {

        redisTemplate.opsForValue().set(
                REFRESH_PREFIX + member.getEmail(),
                tokenDto.getRefreshToken(),
                tokenDto.getRefreshTokenExpiresIn(),
                TimeUnit.MILLISECONDS
        );
    }


    public String getRefreshToken(String email) {

        return (String) redisTemplate.opsForValue().get(REFRESH_PREFIX + email);
    }


    public void deleteRefreshToken(String email) {

        redisTemplate.delete(REFRESH_PREFIX + email);
    }


    public void saveLogoutToken(String accessToken) {

        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + accessToken,
                LOGOUT,
                jwtTokenProvider.getRemainingExpiration(accessToken),
                TimeUnit.MILLISECONDS
        );
    }


    public boolean isBlacklisted(String accessToken) {

        return redisTemplate.hasKey(BLACKLIST_PREFIX + accessToken);
    }


}