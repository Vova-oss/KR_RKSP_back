package com.example.demo.categories.data.role;

import com.example.demo.categories.domain.role.RoleEntity;

import java.util.List;

public interface RoleGateway {
    RoleDataMapper findByRole(ERoles roles);



    RoleEntity createRoleEntity (RoleDataMapper roleDataMapper);
    List<RoleEntity> createListRoleEntity(List<RoleDataMapper> roles);

    List<RoleDataMapper> createListRoleDataMapper(List<RoleEntity> roleEntities);
    List<RoleEntity> findByListId(List<Long> id_roleEntities);

    RoleDataMapper createRoleDataMapper(RoleEntity roleEntity);
}
