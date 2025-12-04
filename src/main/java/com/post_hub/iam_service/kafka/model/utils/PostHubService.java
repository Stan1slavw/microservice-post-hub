package com.post_hub.iam_service.kafka.model.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostHubService {
    GATEWAY_SERVICE("gateway-service"),
    IAM_SERVICE("iam_service"),
    UNDEFINED_SERVICE("undefined_service");

    private final String value;
}
