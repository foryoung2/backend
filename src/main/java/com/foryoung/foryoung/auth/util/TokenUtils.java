package com.foryoung.foryoung.auth.util;

import org.springframework.util.StringUtils;

public final class TokenUtils {


    private static final String BEARER_PREFIX = "Bearer ";


    public static String resolveToken(String authorizationHeader) {

        if (!StringUtils.hasText(authorizationHeader)
                || !authorizationHeader.startsWith(BEARER_PREFIX)) {

            return null;
        }

        return authorizationHeader.substring(BEARER_PREFIX.length());

    }


}