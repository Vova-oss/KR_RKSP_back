package com.example.demo.categories.domain.refresh_token;

import com.example.demo.categories.domain.user.UserEntity;
import lombok.Data;

import java.time.Instant;

@Data
public class RefreshTokenEntity {

    private Long id;
    private Long id_userEntity;
    private String token;
    private Instant expiryDate;

}
