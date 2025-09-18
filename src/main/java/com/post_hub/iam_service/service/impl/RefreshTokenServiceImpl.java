package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.model.entity.RefreshToken;
import com.post_hub.iam_service.model.entity.User;
import com.post_hub.iam_service.repositories.RefreshTokenRepository;
import com.post_hub.iam_service.service.RefreshTokenService;
import com.post_hub.iam_service.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public RefreshToken generateOrUpdateRefreshToken(User user) {
        return refreshTokenRepository.findByUserId(user.getId())
                .map(refreshToken -> {
                    refreshToken.setCreated(LocalDateTime.now());
                    refreshToken.setToken(ApiUtils.generateUuidWithoutDash());
                    return refreshTokenRepository.save(refreshToken);
                })
                .orElseGet(()-> {
                    RefreshToken newToken = new RefreshToken();
                    newToken.setUser(user);
                    newToken.setCreated(LocalDateTime.now());
                    newToken.setToken(ApiUtils.generateUuidWithoutDash());
                    return refreshTokenRepository.save(newToken);
                });
    }
}
