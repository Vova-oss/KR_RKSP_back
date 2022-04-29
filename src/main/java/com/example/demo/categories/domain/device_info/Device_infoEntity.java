package com.example.demo.categories.domain.device_info;

import com.example.demo.categories.domain.device.DeviceEntity;
import lombok.Data;

@Data
public class Device_infoEntity {

    private Long id;
    private String title;
    private String description;
    private Long id_device;

}
