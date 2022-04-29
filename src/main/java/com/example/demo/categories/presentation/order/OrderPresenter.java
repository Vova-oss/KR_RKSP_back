package com.example.demo.categories.presentation.order;

import com.example.demo.categories.domain.order.OrderEntity;

import java.util.List;

public interface OrderPresenter {

    OrderResponseModel createOrderResponseModel(OrderEntity orderEntity);
    List<OrderResponseModel> successResponse(List<OrderEntity> orderEntities);


}
