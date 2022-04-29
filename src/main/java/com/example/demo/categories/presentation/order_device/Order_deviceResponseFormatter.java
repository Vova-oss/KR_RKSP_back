package com.example.demo.categories.presentation.order_device;

import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.device.DeviceInputBoundary;
import com.example.demo.categories.domain.order_device.Order_deviceEntity;
import com.example.demo.categories.domain.order_device.Order_deviceInputBoundary;
import com.example.demo.categories.presentation.device.DevicePresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class Order_deviceResponseFormatter implements Order_devicePresenter{

    @Autowired
    DevicePresenter devicePresenter;
    @Autowired
    DeviceInputBoundary deviceInputBoundary;
    @Autowired
    Order_deviceInputBoundary order_deviceInputBoundary;

    @Override
    public Order_deviceResponseModel createOrder_deviceResponseModel(Long id) {
        Order_deviceEntity order_deviceEntity = order_deviceInputBoundary.findById(id);

        Order_deviceResponseModel order_deviceResponseModel = new Order_deviceResponseModel();
        order_deviceResponseModel.setAmountOfProduct(order_deviceEntity.getAmountOfProduct());

        DeviceEntity deviceEntity = deviceInputBoundary.findById(order_deviceEntity.getId_deviceEntity());
        order_deviceResponseModel.setDeviceResponseModel(devicePresenter.createDeviceResponseModel(deviceEntity));

        return  order_deviceResponseModel;
    }

    @Override
    public List<Order_deviceResponseModel> createListOrder_deviceResponseModel(List<Long> list) {


        List<Order_deviceResponseModel> newList = new LinkedList<>();
        for(Long id: list)
            newList.add(createOrder_deviceResponseModel(id));

        return newList;

    }
}
