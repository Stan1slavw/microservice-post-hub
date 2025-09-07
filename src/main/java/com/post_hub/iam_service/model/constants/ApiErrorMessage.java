package com.post_hub.iam_service.model.constants;

import lombok.Getter;

@Getter
public enum ApiErrorMessage {
    POST_INFO_BY_ID("Receiving post with ID: {} was not found");

    private final String message;

    // ЯВНЫЙ конструктор enum'а
    ApiErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
