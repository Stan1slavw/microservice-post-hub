package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.mapper.UserMapper;
import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.dto.post.PostSearchDTO;
import com.post_hub.iam_service.model.dto.post.UserSearchDTO;
import com.post_hub.iam_service.model.dto.user.UserDTO;
import com.post_hub.iam_service.model.entity.User;
import com.post_hub.iam_service.model.exception.DataExistException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.request.User.NewUserRequest;
import com.post_hub.iam_service.model.request.User.UpdateUserRequest;
import com.post_hub.iam_service.model.request.User.UserSearchRequest;
import com.post_hub.iam_service.model.request.post.PostSearchRequest;
import com.post_hub.iam_service.model.responce.IamResponse;
import com.post_hub.iam_service.model.responce.PaginationResponse;
import com.post_hub.iam_service.repositories.UserRepository;
import com.post_hub.iam_service.repositories.criteria.UserSearchCriteria;
import com.post_hub.iam_service.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public IamResponse<UserDTO> getById(@NotNull Integer userId) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(()-> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND.getMessage(userId)));
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
        user.setPassword(passwordEncoder.encode(newUserRequest.getPassword()));
        User savedUser = userRepository.save(user);
        UserDTO userDTO = userMapper.toDTO(savedUser);

        return IamResponse.createSuccessful(userDTO);
    }

    @Override
    public IamResponse<UserDTO> updateUser(@NotNull Integer userId, @NotNull UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND.getMessage(userId)));
        userMapper.updatePost(user, request);
        user.setUpdated(LocalDateTime.now());
        user = userRepository.save(user);

        UserDTO userDTO = userMapper.toDTO(user);

        return IamResponse.createSuccessful(userDTO);
    }

    @Override
    public void softDeleteUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND.getMessage(userId)));
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public IamResponse<PaginationResponse<UserSearchDTO>> findAllUsers(Pageable pageable) {
        Page<UserSearchDTO> users = userRepository.findAll(pageable)
                .map(userMapper::toUserSearchDTO);

        PaginationResponse<UserSearchDTO> paginationResponse = new PaginationResponse<>(
                users.getContent(),
                new PaginationResponse.Pagination(
                        users.getTotalElements(),
                        pageable.getPageSize(),
                        users.getNumber() + 1,
                        users.getTotalPages()
                )
        );

        return IamResponse.createSuccessful(paginationResponse);
    }

    @Override
    public IamResponse<PaginationResponse<UserSearchDTO>> searchUsers(UserSearchRequest request, Pageable pageable) {
        Specification<User> specification = new UserSearchCriteria(request);

        Page<UserSearchDTO> usersPage = userRepository.findAll(specification, pageable)
                .map(userMapper::toUserSearchDTO);

        PaginationResponse<UserSearchDTO> response = PaginationResponse.<UserSearchDTO>builder()
                .content(usersPage.getContent())
                .pagination(PaginationResponse.Pagination.builder()
                        .total(usersPage.getTotalElements())
                        .limit(pageable.getPageSize())
                        .page(usersPage.getNumber() + 1)
                        .pages(usersPage.getTotalPages())
                        .build())
                .build();

        return IamResponse.createSuccessful(response);

    }
}
