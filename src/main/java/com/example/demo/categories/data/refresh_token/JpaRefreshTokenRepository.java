package com.example.demo.categories.data.refresh_token;

import com.example.demo.categories.data.user.UserDataMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenDataMapper, Long> {

    Optional<RefreshTokenDataMapper> findByToken(String token);
    List<RefreshTokenDataMapper> findAllByUserDataMapper(UserDataMapper userDataMapper);

}
