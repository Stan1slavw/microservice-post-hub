package com.post_hub.iam_service.model.request.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequest {

    @NotNull(message = "Post ID cannot be null")
    private Integer postId;
    @NotNull(message = "Message cannot be null")
    private String message;

}