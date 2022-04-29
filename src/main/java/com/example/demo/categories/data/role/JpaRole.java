package com.example.demo.categories.data.role;

import com.example.demo.categories.data.user.UserDataMapper;
import com.example.demo.categories.domain.role.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

@Component
public class JpaRole implements RoleGateway {

    @Autowired
    JpaRoleRepository jpaRoleRepository;

    @Override
    public RoleDataMapper findByRole(ERoles role) {
        return jpaRoleRepository.findByRole(role);
    }

    @Override
    public List<RoleEntity> createListRoleEntity(List<RoleDataMapper> roles) {
        List<RoleEntity> list = new LinkedList<>();
        for(RoleDataMapper roleDataMapper: roles)
            list.add(createRoleEntity(roleDataMapper));
        return list;
    }

    @Override
    public List<RoleDataMapper> createListRoleDataMapper(List<RoleEntity> roleEntities) {
        if(roleEntities == null)
            return null;

        List<RoleDataMapper> list = new LinkedList<>();
        for(RoleEntity roleEntity: roleEntities)
            list.add(createRoleDataMapper(roleEntity));

        return list;
    }

    @Override
    public List<RoleEntity> findByListId(List<Long> id_roleEntities) {
        if(id_roleEntities == null)
            return null;

        List<RoleDataMapper> list = new LinkedList<>();
        for(Long id: id_roleEntities)
            list.add(jpaRoleRepository.findById(id).orElse(null));

        return createListRoleEntity(list);
    }

    @Override
    public RoleEntity createRoleEntity (RoleDataMapper roleDataMapper){
        if(roleDataMapper == null)
            return null;

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(roleDataMapper.getId());
        roleEntity.setRole(roleDataMapper.getRole());
        return roleEntity;
    }

    @Override
    public RoleDataMapper createRoleDataMapper (RoleEntity roleEntity){
        if(roleEntity == null)
            return null;

        RoleDataMapper roleDataMapper = new RoleDataMapper();
        roleDataMapper.setId(roleEntity.getId());
        roleDataMapper.setRole(roleEntity.getRole());

        return roleDataMapper;
    }

}
