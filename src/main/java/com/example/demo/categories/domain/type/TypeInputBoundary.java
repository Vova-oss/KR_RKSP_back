package com.example.demo.categories.domain.type;

import com.example.demo.categories.presentation.type.TypeResponseModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface TypeInputBoundary {

    void addType(String typeName, HttpServletRequest request, HttpServletResponse response);

    List<TypeResponseModel> getAll();

    void deleteType(String body, HttpServletRequest request, HttpServletResponse response);

    void editType(String body, HttpServletRequest request, HttpServletResponse response);

    TypeEntity findByName(String type);

    TypeEntity findById(Long id_typeEntity);
}
