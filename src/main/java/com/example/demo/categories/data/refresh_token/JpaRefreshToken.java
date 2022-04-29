package com.example.demo.categories.data.refresh_token;

import com.example.demo.categories.data.user.UserDataMapper;
import com.example.demo.categories.data.user.UserGateway;
import com.example.demo.categories.domain.refresh_token.RefreshTokenEntity;
import com.example.demo.categories.domain.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class JpaRefreshToken implements RefreshTokenGateway {

    @Autowired
    JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @Autowired
    UserGateway userGateway;

    @Override
    public RefreshTokenEntity save(RefreshTokenEntity refreshToken) {
        RefreshTokenDataMapper refreshTokenDataMapper = createRefreshTokenDataMapper(refreshToken);
        refreshTokenDataMapper = jpaRefreshTokenRepository.save(refreshTokenDataMapper);
        return createRefreshTokenEntity(refreshTokenDataMapper);
    }

    @Override
    public RefreshTokenEntity findByToken(String refreshToken) {
        RefreshTokenDataMapper refreshTokenDataMapper = jpaRefreshTokenRepository.findByToken(refreshToken).
                orElse(null);
        return refreshTokenDataMapper == null ? null : createRefreshTokenEntity(refreshTokenDataMapper);
    }

    @Override
    public List<RefreshTokenEntity> findAllByUserEntity(UserEntity userEntity) {
        List<RefreshTokenDataMapper> list = jpaRefreshTokenRepository.
                findAllByUserDataMapper(userGateway.createUserDataMapper(userEntity));
        return createListRefreshTokenEntity(list);
    }

    @Override
    public void deleteAll(List<RefreshTokenEntity> list) {
        List<RefreshTokenDataMapper> newList = createListRefreshTokenDataMapper(list);
        jpaRefreshTokenRepository.deleteAll(newList);
    }

    @Override
    public void delete(RefreshTokenEntity refreshTokenEntity) {
        RefreshTokenDataMapper refreshTokenDataMapper = createRefreshTokenDataMapper(refreshTokenEntity);
        jpaRefreshTokenRepository.delete(refreshTokenDataMapper);
    }

    @Override
    public RefreshTokenDataMapper createRefreshTokenDataMapper(RefreshTokenEntity refreshTokenEntity){
        if(refreshTokenEntity == null)
            return null;

        RefreshTokenDataMapper refreshTokenDataMapper = new RefreshTokenDataMapper();
        refreshTokenDataMapper.setId(refreshTokenEntity.getId());
        refreshTokenDataMapper.setToken(refreshTokenEntity.getToken());
        refreshTokenDataMapper.setExpiryDate(refreshTokenEntity.getExpiryDate());
        UserEntity userEntity = userGateway.findById(refreshTokenEntity.getId_userEntity());
        refreshTokenDataMapper.setUserDataMapper(userGateway.createUserDataMapper(userEntity));

        return refreshTokenDataMapper;
    }

    @Override
    public RefreshTokenEntity createRefreshTokenEntity(RefreshTokenDataMapper refreshTokenDataMapper){
        if(refreshTokenDataMapper == null)
            return null;

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setId(refreshTokenDataMapper.getId());
        refreshTokenEntity.setToken(refreshTokenDataMapper.getToken());
        refreshTokenEntity.setExpiryDate(refreshTokenDataMapper.getExpiryDate());
        refreshTokenEntity.setId_userEntity(refreshTokenDataMapper.getUserDataMapper().getId());
        return refreshTokenEntity;
    }

    @Override
    public List<RefreshTokenDataMapper> createListRefreshTokenDataMapper(List<RefreshTokenEntity> list){
        if(list == null)
            return null;

        List<RefreshTokenDataMapper> newList = new LinkedList<>();
        for(RefreshTokenEntity refreshTokenEntity: list)
            newList.add(createRefreshTokenDataMapper(refreshTokenEntity));

        return newList;
    }

    @Override
    public List<RefreshTokenEntity> createListRefreshTokenEntity(List<RefreshTokenDataMapper> list){
        if(list == null)
            return null;

        List<RefreshTokenEntity> newList = new LinkedList<>();
        for(RefreshTokenDataMapper refreshTokenDataMapper: list)
            newList.add(createRefreshTokenEntity(refreshTokenDataMapper));

        return newList;
    }
}
