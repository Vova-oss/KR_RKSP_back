package com.example.demo.categories.data.device_info;

import com.example.demo.categories.data.device.DeviceDataMapper;
import com.example.demo.categories.data.device.DeviceGateway;
import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.device_info.Device_infoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class JpaDevice_info implements Device_infoGateway{

    @Autowired
    JpaDevice_infoRepository jpaDevice_infoRepository;

    @Autowired
    DeviceGateway deviceGateway;

    @Override
    public void save(Device_infoEntity device_infoEntity) {
        jpaDevice_infoRepository.save(createDevice_infoDataMapper(device_infoEntity));
    }

    @Override
    public Device_infoEntity findById(Long id) {
        Device_infoDataMapper device_infoDataMapper = jpaDevice_infoRepository.findById(id).orElse(null);
        return device_infoDataMapper == null ? null : createDevice_infoEntity(device_infoDataMapper);
    }

    @Override
    public Device_infoDataMapper createDevice_infoDataMapper(Device_infoEntity device_infoEntity){
        if(device_infoEntity == null)
            return null;

        Device_infoDataMapper device_infoDataMapper = new Device_infoDataMapper();
        device_infoDataMapper.setId(device_infoEntity.getId());
        device_infoDataMapper.setTitle(device_infoEntity.getTitle());
        device_infoDataMapper.setDescription(device_infoEntity.getDescription());
        DeviceEntity deviceEntity = deviceGateway.findById(device_infoEntity.getId_device());
        device_infoDataMapper.setDeviceDataMapper(deviceGateway.createDeviceDataMapper(deviceEntity));
        return device_infoDataMapper;
    }

    @Override
    public Device_infoEntity createDevice_infoEntity(Device_infoDataMapper device_infoDataMapper){
        if(device_infoDataMapper == null)
            return null;

        Device_infoEntity device_infoEntity = new Device_infoEntity();
        device_infoEntity.setId(device_infoDataMapper.getId());
        device_infoEntity.setTitle(device_infoDataMapper.getTitle());
        device_infoEntity.setDescription(device_infoDataMapper.getDescription());
        return device_infoEntity;
    }

    @Override
    public List<Device_infoDataMapper> createListDevice_infoDataMapper(List<Device_infoEntity> list){
        if(list == null)
            return null;

        List<Device_infoDataMapper> newList = new LinkedList<>();
        for(Device_infoEntity device_infoEntity: list)
            newList.add(createDevice_infoDataMapper(device_infoEntity));

        return newList;
    }

    @Override
    public List<Device_infoEntity> createListDevice_infoEntity(List<Device_infoDataMapper> list){
        if(list == null) {
            return null;
        }

        List<Device_infoEntity> newList = new LinkedList<>();
        for(Device_infoDataMapper device_infoDataMapper: list)
            newList.add(createDevice_infoEntity(device_infoDataMapper));

        return newList;
    }
}
