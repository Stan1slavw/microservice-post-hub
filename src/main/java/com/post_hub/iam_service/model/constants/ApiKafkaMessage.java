package com.post_hub.iam_service.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiKafkaMessage {

    USER_CREATED("User was successfully created"),
    USER_UPDATED("User was successfully updated"),
    USER_DELETED("User waas successfully deleted"),
    POST_CREATED("Post was successfully created"),
    POST_UPDATED("Post was successfully updated"),
    POST_DELETED("Post waas successfully deleted"),
    COMMENT_CREATED("Comment was successfully created"),
    COMMENT_UPDATED("Comment was successfully updated"),
    COMMENT_DELETED("Comment waas successfully deleted"),;

    private final String value;

    public String getMessage(Object... args){
        return String.format(value, args);
    }



}
