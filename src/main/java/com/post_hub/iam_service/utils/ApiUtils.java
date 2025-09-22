package com.post_hub.iam_service.utils;

import com.post_hub.iam_service.model.constants.ApiConstants;
import com.post_hub.iam_service.security.encrypt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class ApiUtils {
    private final JwtTokenProvider jwtTokenProvider;
    public static String getMethodName(){
        try {
            return Thread.currentThread().getStackTrace()[1].getMethodName();
        }catch (Exception cause){
            return ApiConstants.UNDEFINED;
        }
    }

    public static Cookie createAuthCookie(String value){
        Cookie authtorizationCookie = new Cookie(HttpHeaders.AUTHORIZATION, value);
        authtorizationCookie.setHttpOnly(true);
        authtorizationCookie.setSecure(true);
        authtorizationCookie.setPath("/");
        authtorizationCookie.setMaxAge(300);
        return authtorizationCookie;
    }

    public static String generateUuidWithoutDash(){
        return UUID.randomUUID().toString().replace(ApiConstants.DASH, StringUtils.EMPTY);
    }

    public static String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Integer getUserIdFromAuthentication(){
        String jwtToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        return Integer.parseInt(jwtTokenProvider.getUserId(jwtToken));
    }
}
