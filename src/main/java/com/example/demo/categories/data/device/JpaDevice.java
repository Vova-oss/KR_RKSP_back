package com.example.demo.categories.data.device;

import com.example.demo.categories.data.brand.BrandDataMapper;
import com.example.demo.categories.data.brand.BrandGateway;
import com.example.demo.categories.data.device_info.Device_infoDataMapper;
import com.example.demo.categories.data.device_info.Device_infoGateway;
import com.example.demo.categories.data.type.TypeDataMapper;
import com.example.demo.categories.data.type.TypeGateway;
import com.example.demo.categories.domain.brand.BrandEntity;
import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.type.TypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class JpaDevice implements DeviceGateway{

    @Autowired
    JpaDeviceRepository jpaDeviceRepository;

    @Autowired
    TypeGateway typeGateway;
    @Autowired
    BrandGateway brandGateway;
    @Autowired
    Device_infoGateway device_infoGateway;

    @Override
    public boolean existsByName(String name) {
        return jpaDeviceRepository.existsByName(name);
    }

    @Override
    public DeviceEntity findById(Long id) {
        DeviceDataMapper deviceDataMapper = jpaDeviceRepository.findById(id).orElse(null);
        return deviceDataMapper == null ? null :createDeviceEntity(deviceDataMapper);
    }

    @Override
    public DeviceEntity save(DeviceEntity deviceEntity) {
        DeviceDataMapper deviceDataMapper = createDeviceDataMapper(deviceEntity);
        deviceDataMapper = jpaDeviceRepository.save(deviceDataMapper);
        return createDeviceEntity(deviceDataMapper);
    }

    @Override
    public List<DeviceEntity> findAllByTypeIdAndBrandId(TypeEntity typeParent, BrandEntity brandParent) {
        TypeDataMapper typeDataMapper = typeGateway.createTypeDataMapper(typeParent);
        BrandDataMapper brandDataMapper = brandGateway.createBrandDataMapper(brandParent);
        List<DeviceDataMapper> list = jpaDeviceRepository.findAllByTypeDataMapperAndBrandDataMapper(typeDataMapper, brandDataMapper);

        return createListDeviceEntity(list);
    }

    @Override
    public List<DeviceEntity> findAllByTypeEntity(TypeEntity type) {
        List<DeviceDataMapper> list = jpaDeviceRepository.findAllByTypeDataMapper(typeGateway.createTypeDataMapper(type));
        return createListDeviceEntity(list);
    }

    @Override
    public List<DeviceEntity> findAllByBrandEntity(BrandEntity brandEntity) {
        List<DeviceDataMapper> list = jpaDeviceRepository.findAllByBrandDataMapper(brandGateway.createBrandDataMapper(brandEntity));
        return createListDeviceEntity(list);
    }

    @Override
    public List<DeviceEntity> findAll() {
        List<DeviceDataMapper> list = jpaDeviceRepository.findAll();
        return createListDeviceEntity(list);
    }

    @Override
    public void delete(DeviceEntity deviceEntity) {
        DeviceDataMapper deviceDataMapper = createDeviceDataMapper(deviceEntity);
        jpaDeviceRepository.delete(deviceDataMapper);
    }

    @Override
    public DeviceDataMapper createDeviceDataMapper(DeviceEntity deviceEntity){
        if(deviceEntity == null)
            return null;

        DeviceDataMapper deviceDataMapper = new DeviceDataMapper();
        deviceDataMapper.setId(deviceEntity.getId());
        deviceDataMapper.setName(deviceEntity.getName());
        deviceDataMapper.setPrice(deviceEntity.getPrice());
        deviceDataMapper.setPathFile(deviceEntity.getPathFile());
        deviceDataMapper.setIsName(deviceEntity.getIsName());
        TypeEntity typeEntity = typeGateway.findById(deviceEntity.getId_typeEntity());
        deviceDataMapper.setTypeDataMapper(typeGateway.createTypeDataMapper(typeEntity));
        BrandEntity brandEntity = brandGateway.findById(deviceEntity.getId_brandEntity());
        deviceDataMapper.setBrandDataMapper(brandGateway.createBrandDataMapper(brandEntity));
        deviceDataMapper.setDataOfCreate(deviceEntity.getDataOfCreate());

        return deviceDataMapper;
    }

    @Override
    public DeviceEntity createDeviceEntity(DeviceDataMapper deviceDataMapper){
        if(deviceDataMapper == null)
            return null;

        DeviceEntity deviceEntity = new DeviceEntity();
        deviceEntity.setId(deviceDataMapper.getId());
        deviceEntity.setName(deviceDataMapper.getName());
        deviceEntity.setPrice(deviceDataMapper.getPrice());
        deviceEntity.setPathFile(deviceDataMapper.getPathFile());
        deviceEntity.setIsName(deviceDataMapper.getIsName());
        deviceEntity.setId_typeEntity(deviceDataMapper.getTypeDataMapper().getId());
        deviceEntity.setId_brandEntity(deviceDataMapper.getBrandDataMapper().getId());
        deviceEntity.setDataOfCreate(deviceDataMapper.getDataOfCreate());
        if(deviceDataMapper.getDevice_infoDataMappers() == null)
            return deviceEntity;
        List<Long> list = new LinkedList<>();
        for(Device_infoDataMapper device_infoDataMapper: deviceDataMapper.getDevice_infoDataMappers())
            list.add(device_infoDataMapper.getId());
        deviceEntity.setId_device_infoEntities(list);


        return deviceEntity;
    }

    @Override
    public List<DeviceEntity> createListDeviceEntity(List<DeviceDataMapper> list){
        if(list == null)
            return null;

        List<DeviceEntity> newList = new LinkedList<>();
        for(DeviceDataMapper deviceDataMapper: list)
            newList.add(createDeviceEntity(deviceDataMapper));

        return newList;
    }

    @Override
    public List<DeviceDataMapper> createListDeviceDataMapper(List<DeviceEntity> list){
        if(list == null)
            return null;

        List<DeviceDataMapper> newList = new LinkedList<>();
        for(DeviceEntity deviceEntity: list)
            newList.add(createDeviceDataMapper(deviceEntity));

        return newList;
    }

}
