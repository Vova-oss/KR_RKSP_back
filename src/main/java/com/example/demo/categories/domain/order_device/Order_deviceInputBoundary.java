package com.example.demo.categories.domain.order_device;

import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.order.OrderEntity;

public interface Order_deviceInputBoundary {
    void save(OrderEntity orderEntity, DeviceEntity deviceEntity, long amount);
    Order_deviceEntity findById(Long id);
}
