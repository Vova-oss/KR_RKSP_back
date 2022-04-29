package com.example.demo.categories.data.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<UserDataMapper,Long> {
    UserDataMapper findByTelephoneNumber(String telephoneNumber);
    Boolean existsByTelephoneNumber(String telephoneNumber);
}
