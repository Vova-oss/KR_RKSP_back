package com.example.demo.categories.presentation.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseModelInfoAboutUser {
    private String FIO;
    private String telephone_number;
    private Boolean isMan;
}

