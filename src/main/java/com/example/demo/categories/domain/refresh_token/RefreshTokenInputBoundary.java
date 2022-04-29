package com.example.demo.categories.domain.refresh_token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RefreshTokenInputBoundary {


    RefreshTokenEntity createRTbyUserTelephoneNumber(String login);

    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
