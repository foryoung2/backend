package com.foryoung.foryoung.global.jwt;

import com.foryoung.foryoung.auth.service.TokenService;
import com.foryoung.foryoung.auth.util.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {


    private final JwtTokenProvider tokenProvider;

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;


    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        String token = resolveToken((HttpServletRequest) request);

        if (token != null && tokenProvider.validateToken(token)) {

            if (!tokenService.isBlacklisted(token)) {

                String email = tokenProvider.getUsername(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }


    private String resolveToken(HttpServletRequest request) {

        return TokenUtils.resolveToken(
                request.getHeader(HttpHeaders.AUTHORIZATION)
        );
    }


}