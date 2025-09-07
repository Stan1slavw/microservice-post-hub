package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.mapper.PostMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.dto.post.PostDTO;
import com.post_hub.iam_service.model.entity.Post;
import com.post_hub.iam_service.model.exception.DataExistException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.request.post.PostRequest;
import com.post_hub.iam_service.model.request.post.UpdatePostRequest;
import com.post_hub.iam_service.model.responce.IamResponse;
import com.post_hub.iam_service.repositories.PostRepository;
import com.post_hub.iam_service.service.PostService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;



    @Override
    public IamResponse<PostDTO> getById(@NotNull Integer postId) {
        Post post = postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(()-> new NotFoundException(ApiErrorMessage.POST_INFO_BY_ID.getMessage(postId)));
        PostDTO postDTO = postMapper.toPostDTO(post);

        return IamResponse.createSuccessful(postDTO);

    }

    @Override
    public IamResponse<PostDTO> createPost(@NotNull PostRequest postRequest) {
        if (postRepository.existsByTitle(postRequest.getTitle())){
            throw new DataExistException(ApiErrorMessage.POST_ALREADY_EXIST.getMessage(postRequest.getTitle()));
        }
        Post post = postMapper.createdPost(postRequest);
        Post savedPost = postRepository.save(post);
        PostDTO postDTO = postMapper.toPostDTO(savedPost);
        return IamResponse.createSuccessful(postDTO);
    }

    @Override
    public IamResponse<PostDTO> updatePost(@NotNull Integer postId, @NotNull UpdatePostRequest request) {
        Post post = postRepository.findByIdAndDeletedFalse(postId).orElseThrow(()-> new NotFoundException(ApiErrorMessage.POST_INFO_BY_ID.getMessage(postId)));

        postMapper.update(post, request);
        post.setUpdated(LocalDateTime.now());
        post = postRepository.save(post);

        PostDTO postDTO = postMapper.toPostDTO(post);
        return IamResponse.createSuccessful(postDTO);
    }

    @Override
    public void softDeletePost(@NotNull Integer postId) {
        Post post = postRepository.findByIdAndDeletedFalse(postId).orElseThrow(()-> new NotFoundException(ApiErrorMessage.POST_INFO_BY_ID.getMessage(postId)));

        post.setDeleted(true);
        postRepository.save(post);
    }
}
