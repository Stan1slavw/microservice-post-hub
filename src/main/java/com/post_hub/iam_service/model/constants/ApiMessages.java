package com.post_hub.iam_service.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiMessages {
    TOKEN_CREATED_OR_UPDATED("Token created or updated successfully"),
    ;


    private final String message;
}
