package com.example.demo.categories.domain.order_device;

import com.example.demo.categories.data.order_device.Order_deviceGateway;
import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.order.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Order_deviceInteractor implements Order_deviceInputBoundary{

    @Autowired
    Order_deviceGateway order_deviceGateway;

    /**
     * Сохранение сущносит order_device
     * @param orderEntity - Заказ
     * @param deviceEntity - Девайс
     * @param amount - Количество девайсов, добавляемых в заказ
     */
    @Override
    public void save(OrderEntity orderEntity, DeviceEntity deviceEntity, long amount) {
        order_deviceGateway.save(orderEntity, deviceEntity, amount);
    }

    /**
     * Получение сущносит order_device по :id
     * @param id - :id
     */
    @Override
    public Order_deviceEntity findById(Long id) {
        return order_deviceGateway.findById(id);
    }
}
