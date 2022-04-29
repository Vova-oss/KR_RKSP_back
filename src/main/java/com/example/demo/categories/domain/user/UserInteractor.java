package com.example.demo.categories.domain.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.demo.categories.domain.jwt_token.JwtTokenInputBoundary;
import com.example.demo.categories.presentation.user.UserResponseModelInfoAboutUser;
import com.example.demo.controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.service.SendingSMS;
import com.example.demo.singleton.SingletonOne;
import com.example.demo.categories.data.user.UserGateway;
import com.example.demo.categories.presentation.user.UserPresenter;
import com.example.demo.categories.presentation.user.UserResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.example.demo.security.SecurityConstants.*;

@Service
public class UserInteractor implements UserInputBoundary{

    @Autowired
    UserGateway userGateway;
    @Autowired
    JwtTokenInputBoundary jwtTokenInputBoundary;
    @Autowired
    UserPresenter userPresenter;

    @Autowired
    SendingSMS sendingSMS;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * Добавление пользователя
     * @param body [json] поля сущности UserEntity
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - User with this telephoneNumber already exist
     */
    @Override
    public void addUser(String body, HttpServletRequest request, HttpServletResponse response) {

        String fio = StaticMethods.parsingJson(body, "FIO", request, response);
        String password = StaticMethods.parsingJson(body, "password", request, response);
        String telephoneNumber = StaticMethods.parsingJson(body, "telephoneNumber", request, response);


        UserEntity userEntity = userGateway.findByTelephoneNumber(telephoneNumber);
        if(userEntity != null && userEntity.getVerification())
            throw new ResponseStatusException(HttpStatus.valueOf(400), "User with this telephoneNumber already exist");

        if(userEntity == null)
            userEntity = new UserEntity();


        userEntity.setTelephoneNumber(telephoneNumber);
        userEntity.setPassword(bCryptPasswordEncoder.encode(password));
        userEntity.setFIO(fio);
        userEntity.setTimeOfCreation(System.currentTimeMillis());

        // ------------Реальный рандомный код. Теперь 111111, тк рассылка отключена (См. ниже) ------------------------------------------------!!!!!!!!!!!!!!!
//        userEntity.setCode((int) (Math.random() * 1_000_000));
        userEntity.setCode(111111);

        userEntity.setIsMan(null);
        userEntity.setVerification(false);
        userGateway.save(userEntity);

        // ------------Отправка кода через сообщение. Отключено, ибо списывает деньги)) -------------------------------------------------------!!!!!!!!!!!!!!!
//        sendingSMS.createSMS(userEntity.getTelephoneNumber(), String.valueOf(userEntity.getCode()));

        userPresenter.prepareSuccessView(request, response,201, "Created");
    }


    /**
     * Удаление пользователя по полю :telephoneNumber
     * @param telephoneNumber :telephoneNumber Пользователя
     */
    @Override
    public void deleteByTelephoneNumber(String telephoneNumber) {
        UserEntity userEntity = userGateway.findByTelephoneNumber(telephoneNumber);
        userGateway.delete(userEntity);
    }


    /**
     * Поиск Пользователя по полю :telephoneNumber
     * @param telephoneNumber :telephoneNumber Пользователя
     */
    @Override
    public UserEntity findByTelephoneNumber(String telephoneNumber){
        return userGateway.findByTelephoneNumber(telephoneNumber);
    }


    /**
     * Проверка роли пользователя
     * @param request request, в котором должен быть jwToken
     * @code 200 - ResponseEntity<ResponseClass>
     * @code 400 - Incorrect JWT token
     */
    @Override
    public ResponseEntity<UserResponseModel> checkRole(HttpServletRequest request) {

        String tokenWithPrefix = request.getHeader(HEADER_JWT_STRING);
        if(tokenWithPrefix!=null && tokenWithPrefix.startsWith(TOKEN_PREFIX)){
            String token = tokenWithPrefix.replace(TOKEN_PREFIX,"");
            try{
                String role = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                        .build()
                        .verify(token)
                        .getClaim("role").asString();

                return userPresenter.prepareSuccessView(200, role, request.getServletPath());
            }catch (IllegalArgumentException | JWTDecodeException ignored){}

        }

        return userPresenter.prepareFailView(400, "Incorrect JWT token", request.getServletPath());
    }


    /**
     * Подтверждение кода, который пришёл по СМС на указанный при регистрации телефонный номер
     * @param body [json]:
     *             telephoneNumber - телефонный номер;
     *             code - код.
     * @code 201 - Code is confirmed
     * @code 400 - Incorrect JSON
     * @code 400 - User with this :telephoneNumber doesn't exist
     * @code 400 - Incorrect code
     * @code 400 - Code has already been confirmed
     */
    @Override
    public void codeConfirmation(String body, HttpServletRequest request, HttpServletResponse response) {

        String telephoneNumber = StaticMethods.parsingJson(body, "telephoneNumber", request, response);
        String code = StaticMethods.parsingJson(body, "code", request ,response);

        synchronized (SingletonOne.getSingleton()) {
            UserEntity userEntity = userGateway.findByTelephoneNumber(telephoneNumber);
            if (userEntity == null)
                throw new ResponseStatusException(HttpStatus.valueOf(400),"User with this :telephoneNumber doesn't exist");

            if (userEntity.getCode() != null && userEntity.getCode() == Integer.parseInt(code)) {
                userEntity.setCode(null);
                userEntity.setTimeOfCreation(null);
                userEntity.setVerification(true);
                userGateway.save(userEntity);
                userPresenter.prepareSuccessView(request, response, 201, "Code is confirmed");
            } else {
                if (userEntity.getCode() == null)
                    throw new ResponseStatusException(HttpStatus.valueOf(400), "Code has already been confirmed");
                else
                    throw new ResponseStatusException(HttpStatus.valueOf(400), "Incorrect code");
            }
        }

    }


    /**
     * Отправка сообщений со кодом подтверждения
     * @param body = Телефонный номер в telephoneNumber
     */
    @Override
    public void sendSMSForPasswordRecovery(String body, HttpServletRequest request, HttpServletResponse response) {

        String telephoneNumber = StaticMethods.parsingJson(body, "telephoneNumber", request, response);

        UserEntity userEntity = findByTelephoneNumber(telephoneNumber);
        if(userEntity == null || !userEntity.getVerification())
            throw new ResponseStatusException(HttpStatus.valueOf(400), "User with this :telephoneNumber doesn't exist");

        userEntity.setTimeOfCreation(System.currentTimeMillis());

        // ------------Реальный рандомный код. Теперь 111111, тк рассылка отключена (См. ниже) ------------------------------------------------!!!!!!!!!!!!!!!
//        userEntity.setCode((int) (Math.random() * 1_000_000));
        userEntity.setCode(111111);

        // ------------Отправка кода через сообщение. Отключено, ибо списывает деньги)) -------------------------------------------------------!!!!!!!!!!!!!!!
//        sendingSMS.createSMS(telephoneNumber, String.valueOf(userEntity.getCode()));

        userPresenter.prepareSuccessView(request, response, 201,"Code has been sent");
    }


    /**
     * Изменение пароля
     * @param body - старый пароль (oldPassword) и новый пароль (newPassword)
     */
    @Override
    public void changePassword(String body, HttpServletRequest request, HttpServletResponse response) {

        String oldPassword = StaticMethods.parsingJson(body, "oldPassword", request, response);
        String newPassword = StaticMethods.parsingJson(body, "newPassword", request ,response);

        String telephoneNumber = jwtTokenInputBoundary.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity userEntity = userGateway.findByTelephoneNumber(telephoneNumber);
        if(userEntity == null)
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Incorrect jwt-token");

        if (!bCryptPasswordEncoder.matches(oldPassword, userEntity.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect current password");

        userEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userGateway.save(userEntity);
        userPresenter.prepareSuccessView(request, response, 201, "Password have changed");
    }

    /** Поиск всех пользователей */
    @Override
    public List<UserEntity> findAll(){
        return userGateway.findAll();
    }

    /**
     * Удаление пользователя
     * @param userEntity - пользователь, которого необходимо удалить
     */
    @Override
    public void delete(UserEntity userEntity){
        userGateway.delete(userEntity);
    }

    /**
     * Поиск пользователя по :id
     * @param id_userEntity - :id пользователя
     */
    @Override
    public UserEntity findById(Long id_userEntity) {
        return userGateway.findById(id_userEntity);
    }

    /**
     * Проверка переданного пароля с реальным данного пользователя, найденного через jwt-token
     * @param password - пароль, который нужно проверить
     * @return true, если пароль верный; false, если пароль неверный
     */
    @Override
    public Boolean checkPassword(String password, HttpServletRequest request, HttpServletResponse response) {
        String telephoneNumber = jwtTokenInputBoundary.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity userEntity = userGateway.findByTelephoneNumber(telephoneNumber);
        if(userEntity == null)
            return false;

        return bCryptPasswordEncoder.matches(password, userEntity.getPassword());
    }

    @Override
    public void changeGender(String gender, HttpServletRequest request, HttpServletResponse response) {
        String telephoneNumber = jwtTokenInputBoundary.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity userEntity = userGateway.findByTelephoneNumber(telephoneNumber);
        if(userEntity == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect jwt-token");

        userEntity.setIsMan(Boolean.valueOf(gender));
        userGateway.save(userEntity);
        userPresenter.prepareSuccessView(request, response, HttpServletResponse.SC_CREATED, "Gender has been changed");
    }

    @Override
    public void changeFio(String fio, HttpServletRequest request, HttpServletResponse response) {
        String telephoneNumber = jwtTokenInputBoundary.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity userEntity = userGateway.findByTelephoneNumber(telephoneNumber);
        if(userEntity == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect jwt-token");

        userEntity.setFIO(fio);
        userGateway.save(userEntity);
        userPresenter.prepareSuccessView(request, response, HttpServletResponse.SC_CREATED, "FIO has been changed");
    }


    @Override
    public UserResponseModelInfoAboutUser getInfoAboutUser(HttpServletRequest request) {
        String telephoneNumber = jwtTokenInputBoundary.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity userEntity = userGateway.findByTelephoneNumber(telephoneNumber);
        return userPresenter.prepareInfoAboutUser(userEntity);
    }
}
