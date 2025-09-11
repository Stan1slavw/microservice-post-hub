package com.post_hub.iam_service.model.dto.post;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserSearchDTO implements Serializable {

    private Integer id;
    private String username;
    private String email;
    private LocalDateTime created;
    private Boolean isDeleted;

}
