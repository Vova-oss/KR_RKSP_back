package com.example.demo.categories.data.order_device;

import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.order.OrderEntity;
import com.example.demo.categories.domain.order_device.Order_deviceEntity;

import java.util.List;

public interface Order_deviceGateway {

    void save(OrderEntity orderEntity, DeviceEntity deviceEntity, long amount);

    Order_deviceEntity findById(Long id);

    List<Order_deviceEntity> findByListId(List<Long> id_order_deviceEntities);



    Order_deviceDataMapper createOrder_deviceDataMapper (Order_deviceEntity order_deviceEntity);
    Order_deviceEntity createOrder_deviceEntity(Order_deviceDataMapper order_deviceDataMapper);
    List<Order_deviceEntity> createListOrder_deviceEntities(List<Order_deviceDataMapper> list);
    List<Order_deviceDataMapper> createListOrder_deviceDataMapper (List<Order_deviceEntity> list);


}
