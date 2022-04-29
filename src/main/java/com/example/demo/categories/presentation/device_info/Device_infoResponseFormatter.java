package com.example.demo.categories.presentation.device_info;

import com.example.demo.categories.domain.device_info.Device_infoEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class Device_infoResponseFormatter implements Device_infoPresenter{


    @Override
    public List<Device_infoResponseModel> successListDevice_infoResponseModel(List<Device_infoEntity> list) {
        List<Device_infoResponseModel> newList = new LinkedList<>();
        for(Device_infoEntity device_infoEntity: list){
            Device_infoResponseModel device_infoResponseModel = new Device_infoResponseModel();
            device_infoResponseModel.setId(device_infoEntity.getId());
            device_infoResponseModel.setTitle(device_infoEntity.getTitle());
            device_infoResponseModel.setDescription(device_infoEntity.getDescription());
            newList.add(device_infoResponseModel);
        }
        return newList;
    }
}
