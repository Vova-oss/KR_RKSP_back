package com.example.demo.categories.data.device_info;

import com.example.demo.categories.domain.device_info.Device_infoEntity;

import java.util.LinkedList;
import java.util.List;

public interface Device_infoGateway {
    void save(Device_infoEntity device_infoEntity);

    Device_infoEntity findById(Long id);


    Device_infoDataMapper createDevice_infoDataMapper(Device_infoEntity device_infoEntity);
    Device_infoEntity createDevice_infoEntity(Device_infoDataMapper device_infoDataMapper);
    List<Device_infoDataMapper> createListDevice_infoDataMapper(List<Device_infoEntity> list);
    List<Device_infoEntity> createListDevice_infoEntity(List<Device_infoDataMapper> list);



}
