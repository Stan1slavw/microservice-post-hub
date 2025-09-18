package com.post_hub.iam_service.utils;

import com.post_hub.iam_service.model.constants.ApiConstants;
import jakarta.servlet.http.Cookie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import java.util.UUID;

public class ApiUtils {
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
}
