package com.post_hub.iam_service.controller;

import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.dto.user.LoginRequest;
import com.post_hub.iam_service.model.dto.user.UserProfileDto;
import com.post_hub.iam_service.model.responce.IamResponse;
import com.post_hub.iam_service.service.AuthService;
import com.post_hub.iam_service.utils.ApiUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${end.point.auth}")
public class AuthController {
    private final AuthService authService;

    @PostMapping("${end.point.login}")
    public ResponseEntity<?> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<UserProfileDto> result = authService.login(request);
        Cookie authtorizationCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
        response.addCookie(authtorizationCookie);
        return ResponseEntity.ok(result);
    }

    @GetMapping("${end.point.refresh.token}")
    public ResponseEntity<IamResponse<UserProfileDto>> refreshToken(@RequestParam(name = "token") String token, HttpServletResponse response){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<UserProfileDto> result = authService.refreshAccessToken(token);
        Cookie authtorizationCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
        response.addCookie(authtorizationCookie);
        return ResponseEntity.ok(result);

    }
}
