package com.example.demo.categories.presentation.order_device;

import com.example.demo.categories.presentation.device.DeviceResponseModel;
import lombok.Data;

@Data
public class Order_deviceResponseModel {

    private DeviceResponseModel deviceResponseModel;
    private Long amountOfProduct;

}
