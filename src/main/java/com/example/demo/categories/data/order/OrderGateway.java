package com.example.demo.categories.data.order;

import com.example.demo.categories.domain.order.OrderEntity;
import com.example.demo.categories.domain.user.UserEntity;

import java.util.List;

public interface OrderGateway {

    OrderEntity save(OrderEntity orderEntity);

    List<OrderEntity> findAllByUser(UserEntity userEntity);

    List<OrderEntity> findAll();

    OrderEntity findById(Long id);

    OrderEntity createOrderEntity(OrderDataMapper orderDataMapper);
    OrderDataMapper createOrderDataMapper(OrderEntity orderEntity);
    List<OrderEntity> createListOrderEntity(List<OrderDataMapper> list);
    List<OrderDataMapper> createListOrderDataMapper(List<OrderEntity> list);



}
