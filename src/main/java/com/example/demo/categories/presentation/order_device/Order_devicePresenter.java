package com.example.demo.categories.presentation.order_device;

import com.example.demo.categories.domain.order.OrderEntity;
import com.example.demo.categories.domain.order_device.Order_deviceEntity;
import com.example.demo.categories.presentation.order.OrderResponseModel;

import java.util.List;

public interface Order_devicePresenter {

    Order_deviceResponseModel createOrder_deviceResponseModel(Long id);
    List<Order_deviceResponseModel> createListOrder_deviceResponseModel(List<Long> list);

}
