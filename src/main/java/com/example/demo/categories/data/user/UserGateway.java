package com.example.demo.categories.data.user;

import com.example.demo.categories.domain.user.UserEntity;

import java.util.LinkedList;
import java.util.List;

public interface UserGateway {
    void save(UserEntity userEntity);

    void delete(UserEntity userEntity);

    UserEntity findByTelephoneNumber(String telephoneNumber);

    List<UserEntity> findAll();

    UserEntity findById(Long id_userEntity);

    public UserDataMapper createUserDataMapper(UserEntity userEntity);
    public List<UserDataMapper> createListUserDataMapper(List<UserEntity> list);
    public UserEntity createUserEntity(UserDataMapper userDataMapper);
    public List<UserEntity> createListUserEntity(List<UserDataMapper> list);


}
