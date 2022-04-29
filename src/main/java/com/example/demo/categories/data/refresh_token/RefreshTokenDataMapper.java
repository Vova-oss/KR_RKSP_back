package com.example.demo.categories.data.refresh_token;

import com.example.demo.categories.data.user.UserDataMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "os_refresh_token")
public class RefreshTokenDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = ":id токена", name = "id", required = true, example = "5")
    private Long id;


    @ApiModelProperty(notes = "Пользователь, которому принадлежит данный токен", name = "userEntity", required = true)
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserDataMapper userDataMapper;


    @ApiModelProperty(notes = "Токен", name = "token", required = true)
    @Column(nullable = false, unique = true)
    private String token;


    @ApiModelProperty(notes = "Дата, после которой токен перестанет действовать", name = "expiry_date", required = true)
    @Column(nullable = false, name = "expiry_date")
    private Instant expiryDate;

}
