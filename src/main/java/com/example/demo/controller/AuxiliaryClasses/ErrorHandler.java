package com.example.demo.controller.AuxiliaryClasses;

import com.example.demo.categories.presentation.user.UserResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("http://localhost:3000")
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<UserResponseModel> ExceptionOfTokenExpired(ResponseStatusException re, HttpServletRequest request){
        return ResponseEntity.status(re.getStatus().value()).body(new UserResponseModel(re.getStatus().value(), re.getReason(), request.getServletPath()));
    }

}
