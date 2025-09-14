package com.post_hub.iam_service.model.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorMessage {
    POST_INFO_BY_ID("Receiving post with ID: %s was not found"),
    POST_ALREADY_EXIST("Post with Title: %s already exist"),
    USER_NOT_FOUND("USER with ID: %s was not found"),
    USERNAME_ALREADY_EXIST("User with Username: %s already exist"),
    EMAIL_ALREADY_EXIST("User with this Email: %s already exist"),
    USER_ROLE_NOT_FOUND("Role with name: %s was not found");

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
