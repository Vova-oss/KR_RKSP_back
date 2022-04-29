package com.example.demo.categories.data.device;

import com.example.demo.categories.domain.brand.BrandEntity;
import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.type.TypeEntity;

import java.util.List;

public interface DeviceGateway {
    boolean existsByName(String name);

    DeviceEntity findById(Long id);

    DeviceEntity save(DeviceEntity deviceEntity);

    List<DeviceEntity> findAllByTypeIdAndBrandId(TypeEntity typeParent, BrandEntity brandParent);

    List<DeviceEntity> findAllByTypeEntity(TypeEntity type);

    List<DeviceEntity> findAllByBrandEntity(BrandEntity brandEntity);

    List<DeviceEntity> findAll();

    void delete(DeviceEntity deviceEntity);

    DeviceDataMapper createDeviceDataMapper(DeviceEntity device);
    DeviceEntity createDeviceEntity(DeviceDataMapper deviceDataMapper);
    List<DeviceEntity> createListDeviceEntity(List<DeviceDataMapper> list);
    List<DeviceDataMapper> createListDeviceDataMapper(List<DeviceEntity> list);


}
