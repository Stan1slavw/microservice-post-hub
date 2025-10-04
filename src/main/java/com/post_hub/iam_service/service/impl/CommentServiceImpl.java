package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.mapper.CommentMapper;
import com.post_hub.iam_service.mapper.PostMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.dto.comment.CommentSearchDTO;
import com.post_hub.iam_service.model.entity.Comment;
import com.post_hub.iam_service.model.entity.Post;
import com.post_hub.iam_service.model.entity.User;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.request.comment.CommentRequest;
import com.post_hub.iam_service.model.request.comment.CommentSearchRequest;
import com.post_hub.iam_service.model.request.comment.UpdateCommentRequest;
import com.post_hub.iam_service.model.responce.IamResponse;
import com.post_hub.iam_service.model.responce.PaginationResponse;
import com.post_hub.iam_service.repositories.CommentRepository;
import com.post_hub.iam_service.repositories.PostRepository;
import com.post_hub.iam_service.repositories.UserRepository;
import com.post_hub.iam_service.repositories.criteria.CommentSearchCriteria;
import com.post_hub.iam_service.service.CommentService;
import com.post_hub.iam_service.utils.ApiUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ApiUtils apiUtils;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    @Override
    public IamResponse<CommentDTO> getCommentById(@NotNull Integer commentId) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId)));

        return IamResponse.createSuccessful(commentMapper.toDto(comment));
    }

    @Override
    public IamResponse<CommentDTO> createComment(@NotNull CommentRequest request) {
        Integer userId = apiUtils.getUserIdFromAuthentication();
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));
        Post post = postRepository.findByIdAndDeletedFalse(request.getPostId())
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(request.getPostId())));

        Comment comment = commentMapper.createComment(request, user, post);
        comment = commentRepository.save(comment);
        postRepository.save(post);

        return IamResponse.createSuccessful(commentMapper.toDto(comment));
    }

    @Override
    public IamResponse<CommentDTO> updateComment(@NotNull Integer commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(()-> new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId)));

        if (request.getPostId() !=null){
            Post post = postRepository.findByIdAndDeletedFalse(request.getPostId())
                    .orElseThrow(() -> new NotFoundException(ApiErrorMessage.POST_NOT_FOUND_BY_ID.getMessage(request.getPostId())));
            comment.setPost(post);
        }

        commentMapper.updateComment(comment, request);
        comment = commentRepository.save(comment);

        return IamResponse.createSuccessful(commentMapper.toDto(comment));
    }

    @Override
    public void softDeleteComment(@NotNull Integer commentId) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.getMessage(commentId)));

        comment.setDeleted(true);
        commentRepository.save(comment);

        Post post = comment.getPost();
        postRepository.save(post);

        IamResponse.createSuccessful(postMapper.toPostDTO(post));
    }

    @Override
    public IamResponse<PaginationResponse<CommentSearchDTO>> findAllComments(Pageable pageable) {
        Page<CommentSearchDTO> comments = commentRepository.findAll(pageable)
                .map(commentMapper::toCommentSearchDto);

        PaginationResponse<CommentSearchDTO> paginationResponse = new PaginationResponse<>(
                comments.getContent(),
                new PaginationResponse.Pagination(
                        comments.getTotalElements(),
                        comments.getSize(),
                        comments.getNumber() + 1,
                        comments.getTotalPages()
                )
        );

        return IamResponse.createSuccessful(paginationResponse);
    }

    @Override
    public IamResponse<PaginationResponse<CommentSearchDTO>> searchComments(@NotNull CommentSearchRequest request, Pageable pageable) {
        Specification<Comment> specification = new CommentSearchCriteria(request);

        Page<CommentSearchDTO> commentsPage = commentRepository.findAll(specification, pageable)
                .map(commentMapper::toCommentSearchDto);

        PaginationResponse<CommentSearchDTO> response = PaginationResponse.<CommentSearchDTO>builder()
                .content(commentsPage.getContent())
                .pagination((PaginationResponse.Pagination.builder()
                        .total(commentsPage.getTotalElements())
                        .limit(pageable.getPageSize())
                        .page(commentsPage.getNumber() + 1)
                        .pages(commentsPage.getTotalPages())
                        .build())).build();
        
        return IamResponse.createSuccessful(response);
    }
}
