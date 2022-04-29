package com.example.demo.categories.data.brand;


import com.example.demo.categories.domain.brand.BrandEntity;
import com.example.demo.categories.domain.type.TypeEntity;

import java.util.LinkedList;
import java.util.List;

public interface BrandGateway {


    void save(BrandEntity brandEntity);

    BrandEntity findByNameAndTypeId(String brandName, TypeEntity typeEntity);

    BrandEntity findById(Long valueOf);

    void delete(BrandEntity brandEntity);


    BrandEntity createBrandEntity(BrandDataMapper brandDataMapper);
    BrandDataMapper createBrandDataMapper(BrandEntity brandEntity);
    List<BrandEntity> createListBrandEntity(List<BrandDataMapper> list);
    List<BrandDataMapper> createListBrandDataMapper(List<BrandEntity> list);



}
