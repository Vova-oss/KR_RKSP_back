package com.example.demo.categories.presentation.brand;

import com.example.demo.categories.domain.brand.BrandEntity;
import com.example.demo.categories.domain.brand.BrandInputBoundary;
import com.example.demo.categories.domain.brand.BrandInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class BrandResponseFormatter implements BrandPresenter{

    @Autowired
    BrandInputBoundary brandInputBoundary;

    @Override
    public List<BrandResponseModel> createListBrandResponseModel(List<Long> list) {
        List<BrandResponseModel> newList = new LinkedList<>();
        for(Long id : list){
            BrandEntity brandEntity = brandInputBoundary.findById(id);
            BrandResponseModel brandResponseModel = new BrandResponseModel();
            brandResponseModel.setId(brandEntity.getId());
            brandResponseModel.setName(brandEntity.getName());
            newList.add(brandResponseModel);
        }
        return newList;
    }
}
