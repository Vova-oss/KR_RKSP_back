package com.example.demo.categories.domain.user;

import com.example.demo.categories.presentation.user.UserResponseModel;
import com.example.demo.categories.presentation.user.UserResponseModelInfoAboutUser;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserInputBoundary {
    void addUser(String body, HttpServletRequest request, HttpServletResponse response);
    void codeConfirmation(String body, HttpServletRequest request, HttpServletResponse response);
    void sendSMSForPasswordRecovery(String body, HttpServletRequest request, HttpServletResponse response);
    void changePassword(String body, HttpServletRequest request, HttpServletResponse response);
    ResponseEntity<UserResponseModel> checkRole(HttpServletRequest request);
    void deleteByTelephoneNumber(String telephoneNumber);
    UserEntity findByTelephoneNumber(String telephoneNumber);

    List<UserEntity> findAll();

    void delete(UserEntity userEntity);

    UserEntity findById(Long id_userEntity);

    Boolean checkPassword(String password, HttpServletRequest request, HttpServletResponse response);

    void changeGender(String gender, HttpServletRequest request, HttpServletResponse response);

    void changeFio(String fio, HttpServletRequest request, HttpServletResponse response);

    UserResponseModelInfoAboutUser getInfoAboutUser(HttpServletRequest request);
}
