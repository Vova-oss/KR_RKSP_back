package com.example.demo.categories.domain.brand;

import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.type.TypeEntity;
import lombok.Data;

import java.util.List;


@Data
public class BrandEntity {

    private Long id;
    private String name;
    private List<Long> id_deviceEntities;
    private String typeEntity;

}
