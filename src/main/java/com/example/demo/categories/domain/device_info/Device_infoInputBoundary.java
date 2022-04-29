package com.example.demo.categories.domain.device_info;

import org.springframework.stereotype.Component;

import java.util.List;

public interface Device_infoInputBoundary {
    void addDevice_info(Device_infoEntity device_infoEntity);

    List<Device_infoEntity> findByListId(List<Long> id_device_infoEntities);
}
