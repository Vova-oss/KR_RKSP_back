package com.example.demo.categories.presentation.user;

import com.example.demo.categories.domain.user.UserInputBoundary;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Api(tags = "User")
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserInputBoundary userInputBoundary;


    @ApiOperation(value = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Incorrect JSON\n" +
                    "User with this telephoneNumber already exist")
    })
    @PostMapping("/registration")
    public void registration(
            @ApiParam(type = "String",
                      value = "ФИО, Телефонный номер и пароль",
                      example = "{\n\"FIO\":\"Полетаев Владимир Викторович\", " +
                              "\n\"telephoneNumber\": \"+79645932177\",\n\"password\": \"password\"}",
                      required = true)
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){
        userInputBoundary.addUser(body, request, response);
    }

    @ApiOperation(value = "Подтверждение кода")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Code is confirmed"),
            @ApiResponse(code = 400, message = "Incorrect JSON\n" +
                    "User with this :telephoneNumber doesn't exist\n" +
                    "Incorrect code\n" +
                    "Code has already been confirmed")
    })
    @PutMapping("/codeConfirmation")
    public void codeConfirmation(
            @ApiParam(type = "String",
                    value = "Телефонный номер и код подтверждения",
                    example = "{\n\"telephoneNumber\": \"+79645932177\",\n\"code\": \"468175\"}",
                    required = true)
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){

        userInputBoundary.codeConfirmation(body, request, response);

    }

    @ApiOperation(value = "Отправка кода по указанному номеру для восстановления пароля")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Code has been sent"),
            @ApiResponse(code = 400, message = "Incorrect JSON\n" +
                    "User with this :telephoneNumber doesn't exist"),
    })
    @PutMapping("/sendSMSForPasswordRecovery")
    public void sendSMSForPasswordRecovery(
            @ApiParam(type = "String",
                    value = "Телефонный номер",
                    example = "{\n\"telephoneNumber\": \"+79645932177\"}",
                    required = true)
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){

        userInputBoundary.sendSMSForPasswordRecovery(body, request, response);

    }

    @ApiOperation(value = "Изменение пароля")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Password have changed"),
            @ApiResponse(code = 400, message = "Incorrect JSON\n" +
                    "Incorrect current password"),
    })
    @PutMapping("/changePassword")
    public void changePassword(
            @ApiParam(type = "String",
                    value = "Старый пароль и новый пароль",
                    example = "{\n\"oldPassword\": \"pass\",\n\"newPassword\": \"newPassword\"}",
                    required = true)
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){
        userInputBoundary.changePassword(body, request, response);
    }

    @ApiOperation(value = "Проверка роли пользователя (Необходим JWT в хедере запроса)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Incorrect JWT token")
    })
    @GetMapping("/checkRole")
    public ResponseEntity<UserResponseModel> checkRole(HttpServletRequest request){
        return userInputBoundary.checkRole(request);
    }


    @ApiOperation(value = "Удаление пользователя по telephoneNumber", hidden = true)
    @DeleteMapping("/deleteByTelephoneNumber")
    public void delete(@RequestBody String telephoneNumber){
        userInputBoundary.deleteByTelephoneNumber(telephoneNumber);
    }


    @ApiOperation(value = "Проверка пароля на правильность (нужен jwt-token)")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect JSON\n"),
    })
    @GetMapping("/checkPassword/{password}")
    public Boolean checkPassword(
            @ApiParam(type = "String",
                    value = "Пароль",
                    example = "password",
                    required = true)
            @PathVariable("password") String password,
            HttpServletRequest request,
            HttpServletResponse response){

        return userInputBoundary.checkPassword(password, request, response);

    }

    @ApiOperation(value = "Изменение пола (нужен jwt-token)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Gender has been changed"),
            @ApiResponse(code = 400, message = "Incorrect JSON")
    })
    @PutMapping("/changeGender/{gender}")
    public void changeGender(
            @ApiParam(type = "Boolean",
                    value = "Пол, отвечающий на вопрос: Это мужчина? true - мужской, false - женский",
                    example = "true",
                    required = true)
            @PathVariable("gender") String gender,
            HttpServletRequest request,
            HttpServletResponse response){

        userInputBoundary.changeGender(gender, request, response);

    }

    @ApiOperation(value = "Изменение ФИО (нужен jwt-token)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "FIO has been changed"),
            @ApiResponse(code = 400, message = "Incorrect JSON")
    })
    @PutMapping("/changeFIO/{fio}")
    public void changeFIO(
            @ApiParam(type = "String",
                    value = "Новое ФИО пользователя",
                    example = "true",
                    required = true)
            @PathVariable("fio") String fio,
            HttpServletRequest request,
            HttpServletResponse response){

        userInputBoundary.changeFio(fio, request, response);

    }

    @ApiOperation(value = "Получение информации о пользователе (нужен jwt-token)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "FIO has been changed"),
            @ApiResponse(code = 400, message = "Incorrect JSON")
    })
    @GetMapping("/getInfoAboutUser")
    public UserResponseModelInfoAboutUser getInfoAboutUser(
            HttpServletRequest request){

        return userInputBoundary.getInfoAboutUser(request);

    }

}
