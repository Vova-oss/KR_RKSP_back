package com.example.demo.categories.domain.device;

import com.example.demo.categories.domain.brand.BrandEntity;
import com.example.demo.categories.domain.device_info.Device_infoEntity;
import com.example.demo.categories.domain.type.TypeEntity;
import lombok.Data;

import java.util.List;


@Data
public class DeviceEntity {


    private Long id;
    private String name;
    private String price;
    private String pathFile;
    private Boolean isName;
    private Long id_typeEntity;
    private Long id_brandEntity;
//    private List<Rating> ratings;
    private List<Long> id_device_infoEntities;
    private Long dataOfCreate;

}
