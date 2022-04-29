package com.example.demo.categories.data.refresh_token;

import com.example.demo.categories.domain.refresh_token.RefreshTokenEntity;
import com.example.demo.categories.domain.user.UserEntity;

import java.util.LinkedList;
import java.util.List;

public interface RefreshTokenGateway {
    RefreshTokenEntity save(RefreshTokenEntity refreshToken);

    RefreshTokenEntity findByToken(String refreshToken);

    List<RefreshTokenEntity> findAllByUserEntity(UserEntity userEntity);

    void deleteAll(List<RefreshTokenEntity> list);

    void delete(RefreshTokenEntity refreshTokenEntity);



    RefreshTokenDataMapper createRefreshTokenDataMapper(RefreshTokenEntity refreshTokenEntity);
    RefreshTokenEntity createRefreshTokenEntity(RefreshTokenDataMapper refreshTokenDataMapper);
    List<RefreshTokenDataMapper> createListRefreshTokenDataMapper(List<RefreshTokenEntity> list);
    List<RefreshTokenEntity> createListRefreshTokenEntity(List<RefreshTokenDataMapper> list);



}
