package com.example.demo.categories.presentation.order;

import com.example.demo.categories.domain.order.OrderEntity;
import com.example.demo.categories.presentation.order_device.Order_devicePresenter;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class OrderResponseFormatter implements OrderPresenter{

    @Autowired
    Order_devicePresenter order_devicePresenter;

    @Override
    public OrderResponseModel createOrderResponseModel(OrderEntity orderEntity) {
        OrderResponseModel orderResponseModel = new OrderResponseModel();
        orderResponseModel.setId(orderEntity.getId());
        orderResponseModel.setStatus(orderEntity.getStatus());
        orderResponseModel.setTotalSumCheck(orderEntity.getTotalSumCheck());
        orderResponseModel.setDataOfCreate(orderEntity.getDataOfCreate());
        orderResponseModel.setOrder_deviceResponseModels(order_devicePresenter.
                createListOrder_deviceResponseModel(orderEntity.getId_order_deviceEntities()));

        return orderResponseModel;
    }

    @Override
    public List<OrderResponseModel> successResponse(List<OrderEntity> orderEntities) {
        List<OrderResponseModel> list = new LinkedList<>();
        for(OrderEntity orderEntity: orderEntities)
            list.add(createOrderResponseModel(orderEntity));

        list.sort((o1, o2) -> o2.getDataOfCreate().compareTo(o1.getDataOfCreate()));
        return list;
    }
}
