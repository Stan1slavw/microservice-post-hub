package com.post_hub.iam_service.controller;

import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.dto.comment.CommentSearchDTO;
import com.post_hub.iam_service.model.request.comment.CommentRequest;
import com.post_hub.iam_service.model.request.comment.CommentSearchRequest;
import com.post_hub.iam_service.model.request.comment.UpdateCommentRequest;
import com.post_hub.iam_service.model.responce.IamResponse;
import com.post_hub.iam_service.model.responce.PaginationResponse;
import com.post_hub.iam_service.service.CommentService;
import com.post_hub.iam_service.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.point.comment}")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("${end.point.id}")
    public ResponseEntity<IamResponse<CommentDTO>> getCommentById(
            @PathVariable(name = "id") Integer commentId){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<CommentDTO> response = commentService.getCommentById(commentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.point.create}")
    public ResponseEntity<IamResponse<CommentDTO>> createComment(@RequestBody @Valid CommentRequest commentRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<CommentDTO> response = commentService.createComment(commentRequest);
        return ResponseEntity.ok(response);
    }


    @PutMapping("${end.point.id}")
    public ResponseEntity<IamResponse<CommentDTO>> updateComment(
            @PathVariable(name = "id") Integer commentId,
            @RequestBody @Valid UpdateCommentRequest commentRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<CommentDTO> response = commentService.updateComment(commentId, commentRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("${end.point.id}")
    public ResponseEntity<Void> softDeleteComment(@PathVariable(name = "id") Integer commentId){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        commentService.softDeleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("${end.point.all}")
    public ResponseEntity<IamResponse<PaginationResponse<CommentSearchDTO>>> getAllComments(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        Pageable pageable = PageRequest.of(page, size);
        IamResponse<PaginationResponse<CommentSearchDTO>> response = commentService.findAllComments(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.point.search}")
    public ResponseEntity<IamResponse<PaginationResponse<CommentSearchDTO>>> searchComments(
            @RequestBody @Valid CommentSearchRequest commentRequest,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        Pageable pageable = PageRequest.of(page, size);
        IamResponse<PaginationResponse<CommentSearchDTO>> response = commentService.searchComments(commentRequest, pageable);
        return ResponseEntity.ok(response);
    }

}
