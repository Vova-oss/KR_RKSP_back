package com.example.demo.categories.presentation.brand;

import com.example.demo.categories.domain.brand.BrandEntity;

import java.util.List;

public interface BrandPresenter {
    List<BrandResponseModel> createListBrandResponseModel(List<Long> list);
}
