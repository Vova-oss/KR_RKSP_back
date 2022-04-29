package com.example.demo.categories.data.type;

import com.example.demo.categories.domain.type.TypeEntity;

import java.util.LinkedList;
import java.util.List;

public interface TypeGateway {
    boolean existsByName(String name);

    void save(TypeEntity typeEntity);

    List<TypeEntity> findAll();

    TypeEntity findByName(String typeName);

    TypeEntity findById(Long valueOf);

    void delete(TypeEntity typeEntity);



    TypeEntity createTypeEntity(TypeDataMapper typeId);
    TypeDataMapper createTypeDataMapper(TypeEntity typeEntity);
    List<TypeEntity> createListTypeEntity(List<TypeDataMapper> list);
    List<TypeDataMapper> createListTypeDataMapper(List<TypeEntity> list);



}

