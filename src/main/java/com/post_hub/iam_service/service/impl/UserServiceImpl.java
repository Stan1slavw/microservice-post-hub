package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.mapper.UserMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.dto.user.UserDTO;
import com.post_hub.iam_service.model.entity.User;
import com.post_hub.iam_service.model.exception.DataExistException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.request.User.NewUserRequest;
import com.post_hub.iam_service.model.responce.IamResponse;
import com.post_hub.iam_service.repositories.UserRepository;
import com.post_hub.iam_service.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public IamResponse<UserDTO> getById(@NotNull Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND.getMessage(userId)));
        UserDTO userDTO = userMapper.toDTO(user);

        return IamResponse.createSuccessful(userDTO);
    }

    @Override
    public IamResponse<UserDTO> createUser(@NotNull NewUserRequest newUserRequest) {
        if (userRepository.existsByUsername(newUserRequest.getUsername())){
            throw new DataExistException(ApiErrorMessage.USERNAME_ALREADY_EXIST.getMessage(newUserRequest.getUsername()));
        }

        if (userRepository.existsByEmail(newUserRequest.getEmail())){
            throw new DataExistException(ApiErrorMessage.EMAIL_ALREADY_EXIST.getMessage(newUserRequest.getEmail()));
        }

        User user = userMapper.create(newUserRequest);
        User savedUser = userRepository.save(user);
        UserDTO userDTO = userMapper.toDTO(savedUser);

        return IamResponse.createSuccessful(userDTO);
    }
}
