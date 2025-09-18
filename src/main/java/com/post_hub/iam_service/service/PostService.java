package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.post.PostDTO;
import com.post_hub.iam_service.model.dto.post.PostSearchDTO;
import com.post_hub.iam_service.model.request.post.PostRequest;
import com.post_hub.iam_service.model.request.post.PostSearchRequest;
import com.post_hub.iam_service.model.request.post.UpdatePostRequest;
import com.post_hub.iam_service.model.responce.IamResponse;
import com.post_hub.iam_service.model.responce.PaginationResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;

public interface PostService {

    IamResponse<PostDTO> getById(@NotNull Integer postId) throws AccessDeniedException;

    IamResponse<PostDTO> createPost(@NotNull PostRequest postRequest, String username);

    IamResponse<PostDTO> updatePost(@NotNull Integer postId, @NotNull UpdatePostRequest updatePostRequest) throws AccessDeniedException;

    void softDeletePost(@NotNull Integer postId);

    IamResponse<PaginationResponse<PostSearchDTO>> findAllPosts(Pageable pageable);

    IamResponse<PaginationResponse<PostSearchDTO>> searchPosts(@NotNull PostSearchRequest request, Pageable pageable);
}
