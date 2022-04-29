package com.example.demo.categories.domain.user;

import com.example.demo.categories.domain.role.RoleEntity;
import lombok.Data;

import java.util.List;

@Data
public class UserEntity {

    private Long id;
    private String telephoneNumber;
    private String password;
    private String FIO;
    private Boolean verification;
    private Boolean isMan;
    private Long timeOfCreation;
    private Integer code;
    private List<Long> id_roleEntities;

}