package com.example.demo.categories.domain.jwt_token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

import static com.example.demo.security.SecurityConstants.EXPIRATION_TIME_OF_JWT;
import static com.example.demo.security.SecurityConstants.SECRET;

@Component
public class JWTokenInteractor implements JwtTokenInputBoundary{

    /** Получаем имя из токена, который нельзя верифицировать, ибо он просрочен */
    @Override
    public String decodeJWT(String jwt){
        String[] chunks = jwt.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String json = new String(decoder.decode(chunks[1]));

        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("sub");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Создаём JWT */
    @Override
    public String createJWT(String telephoneNumber, String role){
        return JWT.create()
                .withSubject(telephoneNumber)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis()+ EXPIRATION_TIME_OF_JWT))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    /** Получение telephoneNumber из JWT */
    @Override
    public String getNameFromJWT(String token){
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }

    /** Получение роли из JWT */
    @Override
    public String getRoleFromJWT(String token){
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                        .build()
                        .verify(token)
                        .getClaim("role").asString();
    }




}
