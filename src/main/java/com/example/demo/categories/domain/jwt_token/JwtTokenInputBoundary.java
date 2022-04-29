package com.example.demo.categories.domain.jwt_token;

public interface JwtTokenInputBoundary {
    String getNameFromJWT(String replace);

    String getRoleFromJWT(String token);

    String createJWT(String login, String role);

    String decodeJWT(String jwToken);
}
