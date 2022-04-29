package com.example.demo.categories.presentation.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseModel {

    private int status;
    private String info;
    private String path;

}
