package com.foryoung.foryoung.global.jwt;

import com.foryoung.foryoung.auth.dto.JwtTokenResponse;
import com.foryoung.foryoung.member.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {


    private final Key key;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = Duration.ofMinutes(30).toMillis();
    private static final long REFRESH_TOKEN_EXPIRE_TIME = Duration.ofDays(14).toMillis();

    private static final String AUTH_CLAIM = "auth";
    private static final String BEARER_PREFIX = "Bearer ";


    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public JwtTokenResponse generateToken(Member member) {

        long now = System.currentTimeMillis();

        Date accessExpiration = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshExpiration = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = createAccessToken(member, accessExpiration);
        String refreshToken = createRefreshToken(member, refreshExpiration);

        return JwtTokenResponse.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }


    public boolean validateToken(String token) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT validation failed : {}", e.getMessage());

            return false;
        }
    }


    public String getUsername(String token) {

        return extractClaims(token).getSubject();
    }


    public long getRemainingExpiration(String token) {

        return extractClaims(token)
                .getExpiration()
                .getTime() - System.currentTimeMillis();
    }


    private String createAccessToken(Member member,
                                     Date expiration) {

        return Jwts.builder()
                .setSubject(member.getEmail())
                .claim(AUTH_CLAIM, member.getRole().name())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    private String createRefreshToken(Member member,
                                      Date expiration) {

        return Jwts.builder()
                .setSubject(member.getEmail())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    private Claims extractClaims(String token) {

        try {

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}