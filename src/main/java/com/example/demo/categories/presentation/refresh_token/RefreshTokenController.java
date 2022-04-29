package com.example.demo.categories.presentation.refresh_token;

import com.example.demo.categories.domain.refresh_token.RefreshTokenInputBoundary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Api(tags = "Security")
@CrossOrigin("http://localhost:3000")
public class RefreshTokenController {

    @Autowired
    RefreshTokenInputBoundary refreshTokenInputBoundary;


    @ApiOperation(value = "Обновление токенов (В хедере необходимы два токена с именами ExpiredJWT и RefreshToken")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 400, message = "Refresh token doesn't exist\n" +
                    "Refresh token was expired")
    })
    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        refreshTokenInputBoundary.refreshToken(request, response);
    }
}
