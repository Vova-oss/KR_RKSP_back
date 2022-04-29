package com.example.demo.categories.presentation.device;

import lombok.Data;

import java.util.List;

@Data
public class DeviceWithParamsResponseModel {

    List<DeviceResponseModel> list;
    int amountOfAllDevices;
    String maxPrice;
    String minPrice;

}
