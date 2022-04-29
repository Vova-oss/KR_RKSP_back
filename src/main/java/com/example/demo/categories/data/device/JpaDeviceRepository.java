package com.example.demo.categories.data.device;

import com.example.demo.categories.data.brand.BrandDataMapper;
import com.example.demo.categories.data.type.TypeDataMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaDeviceRepository extends JpaRepository<DeviceDataMapper, Long> {

    Boolean existsByName(String name);
    List<DeviceDataMapper> findAllByTypeDataMapperAndBrandDataMapper(TypeDataMapper typeId, BrandDataMapper brandId);
    List<DeviceDataMapper> findAllByTypeDataMapper(TypeDataMapper typeId);
    List<DeviceDataMapper> findAllByBrandDataMapper(BrandDataMapper brandId);
    DeviceDataMapper findByName(String name);

}
