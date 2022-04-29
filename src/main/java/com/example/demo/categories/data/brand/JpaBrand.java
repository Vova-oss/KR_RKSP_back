package com.example.demo.categories.data.brand;

import com.example.demo.categories.data.type.TypeDataMapper;
import com.example.demo.categories.data.type.TypeGateway;
import com.example.demo.categories.domain.brand.BrandEntity;
import com.example.demo.categories.domain.type.TypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class JpaBrand implements BrandGateway{

    @Autowired
    JpaBrandRepository jpaBrandRepository;
    @Autowired
    TypeGateway typeGateway;
    
    @Override
    public void save(BrandEntity brandEntity) {
        BrandDataMapper brandDataMapper = createBrandDataMapper(brandEntity);
        jpaBrandRepository.save(brandDataMapper);
    }

    @Override
    public BrandEntity findByNameAndTypeId(String brandName, TypeEntity type) {
        TypeDataMapper typeDataMapper = typeGateway.createTypeDataMapper(type);
        BrandDataMapper brandDataMapper = jpaBrandRepository.findByNameAndTypeDataMapper(brandName, typeDataMapper);
        return createBrandEntity(brandDataMapper);
    }

    @Override
    public BrandEntity findById(Long valueOf) {
        BrandDataMapper brandDataMapper = jpaBrandRepository.findById(valueOf).orElse(null);
        return createBrandEntity(brandDataMapper);

    }

    @Override
    public void delete(BrandEntity brandEntity) {
        BrandDataMapper brandDataMapper = createBrandDataMapper(brandEntity);
        jpaBrandRepository.delete(brandDataMapper);
    }


    @Override
    public BrandEntity createBrandEntity(BrandDataMapper brandDataMapper){
        if(brandDataMapper == null)
            return null;
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(brandDataMapper.getId());
        brandEntity.setName(brandDataMapper.getName());
        brandEntity.setTypeEntity(brandDataMapper.getTypeDataMapper().getName());
        return brandEntity;
    }

    @Override
    public BrandDataMapper createBrandDataMapper(BrandEntity brandEntity){
        if(brandEntity == null)
            return null;
        BrandDataMapper brandDataMapper = new BrandDataMapper();
        brandDataMapper.setId(brandEntity.getId());
        brandDataMapper.setName(brandEntity.getName());
        brandDataMapper.setTypeDataMapper(typeGateway.createTypeDataMapper(typeGateway.findByName(brandEntity.getTypeEntity())));

        return brandDataMapper;
    }

    @Override
    public List<BrandEntity> createListBrandEntity(List<BrandDataMapper> list) {
        if(list == null)
            return null;

        List<BrandEntity> newList = new LinkedList<>();
        for(BrandDataMapper brandDataMapper: list)
            newList.add(createBrandEntity(brandDataMapper));

        return newList;
    }

    @Override
    public List<BrandDataMapper> createListBrandDataMapper(List<BrandEntity> list) {
        if(list == null)
            return null;

        List<BrandDataMapper> newList = new LinkedList<>();
        for(BrandEntity brandEntity: list)
            newList.add(createBrandDataMapper(brandEntity));

        return newList;
    }
}
