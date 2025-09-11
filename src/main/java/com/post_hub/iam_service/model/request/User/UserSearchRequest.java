package com.post_hub.iam_service.model.request.User;

import com.post_hub.iam_service.model.enums.UserSortedFields;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserSearchRequest implements Serializable {
    private String username;
    private String email;

    private Boolean deleted;
    private String keyword;
    private UserSortedFields sortField;

}
