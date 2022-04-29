package com.example.demo.categories.presentation.type;

import com.example.demo.categories.presentation.brand.BrandResponseModel;
import lombok.Data;

import java.util.List;

@Data
public class TypeResponseModel {

    private Long id;
    private String name;
    private List<BrandResponseModel> brands;

}
