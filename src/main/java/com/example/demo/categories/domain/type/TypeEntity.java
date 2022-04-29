package com.example.demo.categories.domain.type;

import com.example.demo.categories.domain.brand.BrandEntity;
import com.example.demo.categories.domain.device.DeviceEntity;
import lombok.Data;

import java.util.List;

@Data
public class TypeEntity {

    private Long id;
    private String name;
//    private List<DeviceEntity> devices;
    private List<Long> id_brands;

}
