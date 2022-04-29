package com.example.demo.categories.domain.refresh_token;

import com.example.demo.controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.categories.data.refresh_token.RefreshTokenGateway;
import com.example.demo.categories.data.role.RoleGateway;
import com.example.demo.categories.domain.jwt_token.JwtTokenInputBoundary;
import com.example.demo.categories.domain.role.RoleEntity;
import com.example.demo.categories.domain.user.UserEntity;
import com.example.demo.categories.domain.user.UserInputBoundary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.example.demo.security.SecurityConstants.*;

@Component
public class RefreshTokenInteractor implements RefreshTokenInputBoundary{

    @Autowired
    RefreshTokenGateway refreshTokenGateway;

    @Autowired
    UserInputBoundary userInputBoundary;
    @Autowired
    JwtTokenInputBoundary jwtTokenInputBoundary;
    @Autowired
    RoleGateway roleGateway;

    /** Создание нового refresh-токена */
    @Override
    public RefreshTokenEntity createRTbyUserTelephoneNumber(String telephoneNumber){
        UserEntity userEntity = userInputBoundary.findByTelephoneNumber(telephoneNumber);

        RefreshTokenEntity refreshToken = new RefreshTokenEntity();

        refreshToken.setId_userEntity(userEntity.getId());
        refreshToken.setExpiryDate(Instant.now().plusMillis(EXPIRATION_TIME_OF_RT));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenGateway.save(refreshToken);

        return refreshToken;
    }

    /** Нахождение объекта refreshToken в БД, через его поле token */
    private RefreshTokenEntity findByToken(String refreshToken){
        return refreshTokenGateway.findByToken(refreshToken);
    }


    /** Верификация refresh-токена */
    private RefreshTokenEntity verifyExpiration(RefreshTokenEntity refreshTokenEntity) {
        if (refreshTokenEntity.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenGateway.delete(refreshTokenEntity);
            return null;
        }

        // Обновляю время просрочки и ставлю новый токен
        refreshTokenEntity.setExpiryDate(Instant.now().plusMillis(EXPIRATION_TIME_OF_RT));
        refreshTokenEntity.setToken(UUID.randomUUID().toString());

        refreshTokenEntity = refreshTokenGateway.save(refreshTokenEntity);
        return refreshTokenEntity;
    }


    /** Удаление всех токенов, принадлежащих конкретному пользователю */
    private void deleteAllByUser(UserEntity userEntity) {
        List<RefreshTokenEntity> list = refreshTokenGateway.findAllByUserEntity(userEntity);
        refreshTokenGateway.deleteAll(list);
    }


    /**
     * Обновление refresh-токена и jwt-токена (от клиента нужны они оба. В ответе в хедер запихнутся 2 новых)
     * @code 201 - Created
     * @code 400 - Refresh token doesn't exist
     * @code 400 - Refresh token was expired
     * */
    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){

        String rToken = request.getHeader(HEADER_RT_STRING);
        String jwToken = request.getHeader(HEADER_EXPIRED_JWT_STRING);
        if(rToken!=null && jwToken!=null && !rToken.equals("null") && !jwToken.equals("null")){
            RefreshTokenEntity refreshToken = findByToken(rToken);
            if(refreshToken == null){
                String telephoneNumber = jwtTokenInputBoundary.decodeJWT(jwToken);
                UserEntity userEntity = userInputBoundary.findByTelephoneNumber(telephoneNumber);
                if(userEntity!=null){
                    deleteAllByUser(userEntity);
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token doesn't exist");
            }
            refreshToken = verifyExpiration(refreshToken);
            if(refreshToken == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token was expired");


            UserEntity userEntity = userInputBoundary.findById(refreshToken.getId_userEntity());

            List<RoleEntity> list = roleGateway.findByListId(userEntity.getId_roleEntities());
            String token = jwtTokenInputBoundary.createJWT(userEntity.getTelephoneNumber(), String.valueOf(list.get(0).getRole()));
            response.addHeader(HEADER_JWT_STRING, TOKEN_PREFIX+token);
            response.addHeader(HEADER_RT_STRING, refreshToken.getToken());

            //Устанавливаем, какие хедеры может видеть фронт
            response.addHeader("Access-Control-Expose-Headers", HEADER_JWT_STRING+","+HEADER_RT_STRING);

            StaticMethods.createResponse(request, response,
                    HttpServletResponse.SC_CREATED, "Created");

        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh or JWT token is null");

    }

}
