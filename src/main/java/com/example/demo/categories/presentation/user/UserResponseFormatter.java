package com.example.demo.categories.presentation.user;

import com.example.demo.categories.domain.user.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserResponseFormatter implements UserPresenter {
    @Override
    public void prepareSuccessView(HttpServletRequest request,
                                                HttpServletResponse response,
                                                int status,
                                                String info) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", status);
        body.put("info", info);
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(response.getOutputStream(), body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResponseEntity<UserResponseModel> prepareSuccessView(int status, String role, String path) {
        return ResponseEntity.ok(new UserResponseModel(200, role, path));
    }

    @Override
    public ResponseEntity<UserResponseModel> prepareFailView(int status, String role, String path) {
        return ResponseEntity.status(400).body(new UserResponseModel(400, "Incorrect JWT token", path));
    }

    @Override
    public UserResponseModelInfoAboutUser prepareInfoAboutUser(UserEntity userEntity) {
        UserResponseModelInfoAboutUser user = new UserResponseModelInfoAboutUser();
        user.setFIO(userEntity.getFIO());
        user.setTelephone_number(userEntity.getTelephoneNumber());
        user.setIsMan(userEntity.getIsMan());
        return user;
    }


}
