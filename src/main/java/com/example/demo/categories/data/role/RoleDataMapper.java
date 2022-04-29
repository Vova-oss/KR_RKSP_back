package com.example.demo.categories.data.role;

import com.example.demo.categories.data.user.UserDataMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_role")
public class RoleDataMapper  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = ":id Роли", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Роль (ERoles)", name = "role", required = true, example = "USER")
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ERoles role;


    @ApiModelProperty(notes = "Пользователи, к которому относится данная роль", name = "name")
    @JsonIgnore
    @ManyToMany(mappedBy = "roleDataMappers", fetch = FetchType.LAZY)
    private List<UserDataMapper> userDataMappers;

}
