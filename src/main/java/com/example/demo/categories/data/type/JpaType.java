package com.example.demo.categories.data.type;

import com.example.demo.categories.data.brand.BrandDataMapper;
import com.example.demo.categories.data.brand.BrandGateway;
import com.example.demo.categories.domain.type.TypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class JpaType implements TypeGateway{

    @Autowired
    JpaTypeRepository jpaTypeRepository;
    @Autowired
    BrandGateway brandGateway;

    @Override
    public boolean existsByName(String name) {
        return jpaTypeRepository.existsByName(name);
    }

    @Override
    public void save(TypeEntity typeEntity) {
        TypeDataMapper typeDataMapper = createTypeDataMapper(typeEntity);
        jpaTypeRepository.save(typeDataMapper);
    }

    @Override
    public List<TypeEntity> findAll() {
        return createListTypeEntity(jpaTypeRepository.findAll());
    }

    @Override
    public TypeEntity findByName(String typeName) {
        TypeDataMapper typeDataMapper = jpaTypeRepository.findByName(typeName);
        if(typeDataMapper == null)
            return null;
        return createTypeEntity(typeDataMapper);
    }

    @Override
    public TypeEntity findById(Long valueOf) {
        TypeDataMapper typeDataMapper = jpaTypeRepository.findById(valueOf).orElse(null);
        if(typeDataMapper != null)
            return createTypeEntity(typeDataMapper);
        else return null;
    }

    @Override
    public void delete(TypeEntity typeEntity) {
        TypeDataMapper typeDataMapper = createTypeDataMapper(typeEntity);
        jpaTypeRepository.delete(typeDataMapper);
    }

    @Override
    public TypeDataMapper createTypeDataMapper(TypeEntity typeEntity){
        if(typeEntity == null)
            return null;
        TypeDataMapper typeDataMapper = new TypeDataMapper();
        typeDataMapper.setId(typeEntity.getId());
        typeDataMapper.setName(typeEntity.getName());

        return typeDataMapper;
    }

    @Override
    public TypeEntity createTypeEntity(TypeDataMapper typeDataMapper){
        if(typeDataMapper == null)
            return null;

        TypeEntity typeEntity = new TypeEntity();
        typeEntity.setId(typeDataMapper.getId());
        typeEntity.setName(typeDataMapper.getName());
        List<Long> id_brandsEntity = new LinkedList<>();
        if(typeDataMapper.getBrandDataMappers() != null)
            for(BrandDataMapper brandDataMapper: typeDataMapper.getBrandDataMappers())
                id_brandsEntity.add(brandDataMapper.getId());
        typeEntity.setId_brands(id_brandsEntity);
        return typeEntity;
    }

    @Override
    public List<TypeEntity> createListTypeEntity(List<TypeDataMapper> list){
        if(list == null)
            return null;

        List<TypeEntity> newList = new LinkedList<>();
        for(TypeDataMapper typeDataMapper: list){
            newList.add(createTypeEntity(typeDataMapper));
        }
        return newList;
    }

    @Override
    public List<TypeDataMapper> createListTypeDataMapper(List<TypeEntity> list){
        if(list == null)
            return null;

        List<TypeDataMapper> newList = new LinkedList<>();
        for(TypeEntity typeEntity: list){
            newList.add(createTypeDataMapper(typeEntity));
        }
        return newList;
    }
}
