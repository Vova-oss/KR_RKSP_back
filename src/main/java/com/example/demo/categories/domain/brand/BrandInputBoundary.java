package com.example.demo.categories.domain.brand;

import com.example.demo.categories.domain.type.TypeEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BrandInputBoundary {

    void addBrand(String body, HttpServletRequest request, HttpServletResponse response);

    void deleteBrand(String body, HttpServletRequest request, HttpServletResponse response);

    void editBrand(String body, HttpServletRequest request, HttpServletResponse response);

    BrandEntity findByNameAndTypeId(String brandName, TypeEntity typeEntity);

    BrandEntity findById(Long id);
}

