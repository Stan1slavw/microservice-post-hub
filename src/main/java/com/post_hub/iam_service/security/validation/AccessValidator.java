package com.post_hub.iam_service.security.validation;

import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.entity.User;
import com.post_hub.iam_service.model.exception.DataExistException;
import com.post_hub.iam_service.model.exception.InvalidDataException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.repositories.UserRepository;
import com.post_hub.iam_service.service.model.IamServiceUserRole;
import com.post_hub.iam_service.utils.ApiUtils;
import com.post_hub.iam_service.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
public class AccessValidator {
    private final UserRepository userRepository;
    private final ApiUtils apiUtils;

    public void validateNewUser(String username, String email, String password, String confirmPassword){

        userRepository.findByUsername(username).ifPresent(existingUser -> {
            throw new DataExistException(ApiErrorMessage.USER_ALREADY_EXISTS.getMessage(username));
        });

        userRepository.findByEmail(email).ifPresent(existingUser -> {
            throw new DataExistException(ApiErrorMessage.USER_ALREADY_EXISTS.getMessage(email));
        });

        if (!password.equals(confirmPassword)) {
            throw new InvalidDataException(ApiErrorMessage.MISMATCH_PASSWORDS.getMessage());
        }

        if (PasswordUtils.isNotValidPassword(password)) {
            throw new InvalidDataException(ApiErrorMessage.INVALID_PASSWORD.getMessage());
        }
    }


    public boolean isAdminOrSuperAdmin(Integer userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));

        return user.getRoles().stream().map(role -> IamServiceUserRole.fromName(role.getName()))
                .anyMatch(role -> role == IamServiceUserRole.ADMIN || role == IamServiceUserRole.SUPER_ADMIN);
    }

    @SneakyThrows
    public void validateAdminOrOwnerAccess(Integer ownerId){
        Integer currentUserId = apiUtils.getUserIdFromAuthentication();

        if (!currentUserId.equals(ownerId)
                && !isAdminOrSuperAdmin(currentUserId)) {
            throw new AccessDeniedException(ApiErrorMessage.HAVE_NO_ACCESS.getMessage());
        }
    }
}
