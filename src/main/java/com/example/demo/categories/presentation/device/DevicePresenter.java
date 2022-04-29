package com.example.demo.categories.presentation.device;

import com.example.demo.categories.domain.device.DeviceEntity;

import java.util.List;

public interface DevicePresenter {
    List<DeviceResponseModel> createListDeviceResponseModel(List<DeviceEntity> list);
    DeviceResponseModel createDeviceResponseModel(DeviceEntity deviceEntity);
    DeviceWithParamsResponseModel successDeviceWithParamsResponseModel(List<DeviceEntity> list,
                                                                       int amountOfAllDevices,
                                                                       String maxPrice,
                                                                       String minPrice);
}
