package com.example.demo.categories.data.user;

import com.example.demo.categories.data.role.ERoles;
import com.example.demo.categories.data.role.RoleDataMapper;
import com.example.demo.categories.data.role.RoleGateway;
import com.example.demo.categories.domain.user.UserEntity;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JpaUser implements UserGateway {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JpaUserRepository jpaUserRepository;
    @Autowired
    RoleGateway roleGateway;

    @Override
    public void save(UserEntity userEntity) {
        UserDataMapper userDataMapper = createUserDataMapper(userEntity);
        List<RoleDataMapper> role = new ArrayList<>();
        role.add(roleGateway.findByRole(ERoles.valueOf("USER")));
        userDataMapper.setRoleDataMappers(role);
        jpaUserRepository.save(userDataMapper);
    }

    @Override
    public void delete(UserEntity userEntity) {
        UserDataMapper userDataMapper = createUserDataMapper(userEntity);
        jpaUserRepository.delete(userDataMapper);
    }

    @Override
    public UserEntity findByTelephoneNumber(String telephoneNumber) {
        UserDataMapper userDataMapper = jpaUserRepository.findByTelephoneNumber(telephoneNumber);
        if(userDataMapper != null)
            return createUserEntity(userDataMapper);
        else return null;
    }

    @Override
    public List<UserEntity> findAll() {
        List<UserDataMapper> list = jpaUserRepository.findAll();
        return createListUserEntity(list);
    }

    @Override
    public UserEntity findById(Long id_userEntity) {
        UserDataMapper userDataMapper = jpaUserRepository.findById(id_userEntity).orElse(null);
        return userDataMapper == null ? null : createUserEntity(userDataMapper);
    }

    @Override
    public UserDataMapper createUserDataMapper(UserEntity userEntity){
        if(userEntity == null)
            return null;
        UserDataMapper userDataMapper= new UserDataMapper();
        userDataMapper.setId(userEntity.getId());
        userDataMapper.setTelephoneNumber(userEntity.getTelephoneNumber());
        userDataMapper.setPassword(userEntity.getPassword());
        userDataMapper.setFIO(userEntity.getFIO());
        userDataMapper.setVerification(userEntity.getVerification());
        userDataMapper.setIsMan(userEntity.getIsMan());
        userDataMapper.setTimeOfCreation(userEntity.getTimeOfCreation());
        userDataMapper.setCode(userEntity.getCode());
        List<Long> listId = userEntity.getId_roleEntities();

        userDataMapper.setRoleDataMappers(roleGateway.createListRoleDataMapper(roleGateway.findByListId(listId)));

        return userDataMapper;
    }

    @Override
    public List<UserDataMapper> createListUserDataMapper(List<UserEntity> list){
        if(list == null)
            return null;

        List<UserDataMapper> newList = new LinkedList<>();
        for(UserEntity userEntity:list){
            newList.add(createUserDataMapper(userEntity));
        }
        return newList;
    }

    @Override
    public UserEntity createUserEntity(UserDataMapper userDataMapper){
        if(userDataMapper == null)
            return null;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDataMapper.getId());
        userEntity.setTelephoneNumber(userDataMapper.getTelephoneNumber());
        userEntity.setPassword(userDataMapper.getPassword());
        userEntity.setFIO(userDataMapper.getFIO());
        userEntity.setVerification(userDataMapper.getVerification());
        userEntity.setIsMan(userDataMapper.getIsMan());
        userEntity.setTimeOfCreation(userDataMapper.getTimeOfCreation());
        userEntity.setCode(userDataMapper.getCode());
        userEntity.setId_roleEntities(userDataMapper.getRoleDataMappers().stream().map(RoleDataMapper::getId).collect(Collectors.toList()));
        return userEntity;
    }

    @Override
    public List<UserEntity> createListUserEntity(List<UserDataMapper> list){
        if(list == null)
            return null;

        List<UserEntity> newList = new LinkedList<>();
        for(UserDataMapper userDataMapper: list){
            newList.add(createUserEntity(userDataMapper));
        }
        return newList;
    }

}
