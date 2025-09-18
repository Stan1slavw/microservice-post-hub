package com.post_hub.iam_service.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegistrationUserRequest implements Serializable {
    @NotBlank
    private String username;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotEmpty
    private String confirmPassword;
}
