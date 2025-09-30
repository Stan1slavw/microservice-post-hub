package com.post_hub.iam_service.controller;

import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.request.comment.CommentRequest;
import com.post_hub.iam_service.model.request.comment.UpdateCommentRequest;
import com.post_hub.iam_service.model.responce.IamResponse;
import com.post_hub.iam_service.service.CommentService;
import com.post_hub.iam_service.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
