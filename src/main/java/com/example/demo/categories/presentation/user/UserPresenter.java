package com.example.demo.categories.presentation.user;

import com.example.demo.categories.domain.user.UserEntity;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserPresenter {
    void prepareSuccessView(HttpServletRequest request,
                            HttpServletResponse response,
                            int status,
                            String info);

    ResponseEntity<UserResponseModel> prepareSuccessView(int status, String role, String path);
    ResponseEntity<UserResponseModel> prepareFailView(int status, String role, String path);

    UserResponseModelInfoAboutUser prepareInfoAboutUser(UserEntity userEntity);
}
