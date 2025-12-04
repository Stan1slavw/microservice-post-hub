package com.post_hub.iam_service.controller;

import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.dto.user.LoginRequest;
import com.post_hub.iam_service.model.dto.user.RegistrationUserRequest;
import com.post_hub.iam_service.model.dto.user.UserProfileDto;
import com.post_hub.iam_service.model.responce.IamResponse;
import com.post_hub.iam_service.service.AuthService;
import com.post_hub.iam_service.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.mapstruct.Context;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${end.point.auth}")
@Tag(name = "Authentication Controller", description = "Endpoints for user authentication and registration")
public class AuthController {
    private final AuthService authService;

    @PostMapping("${end.point.login}")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful login",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"token\" : ")))})

    @Operation(summary = "User login endpoint", description = "Endpoint for user authentication and token generation")
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
    @Operation(summary = "Refresh access token endpoint", description = "Endpoint for refreshing the access token using a refresh token")
    public ResponseEntity<IamResponse<UserProfileDto>> refreshToken(@RequestParam(name = "token") String token, HttpServletResponse response){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<UserProfileDto> result = authService.refreshAccessToken(token);
        Cookie authtorizationCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
        response.addCookie(authtorizationCookie);
        return ResponseEntity.ok(result);
    }

    @PostMapping("${end.point.register}")
    @Operation(summary = "User registration endpoint", description = "Endpoint for registering a new user")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationUserRequest request,
                                      HttpServletResponse response){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<UserProfileDto> result = authService.registerUser(request);
        Cookie registrationCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
        response.addCookie(registrationCookie);

        return ResponseEntity.ok(result);
    }
}
