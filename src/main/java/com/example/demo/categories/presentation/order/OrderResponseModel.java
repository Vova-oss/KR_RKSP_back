package com.example.demo.categories.presentation.order;

import com.example.demo.categories.domain.order.EStatusOfOrder;
import com.example.demo.categories.presentation.order_device.Order_deviceResponseModel;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponseModel {

    private Long id;
    private List<Order_deviceResponseModel> order_deviceResponseModels;
    private EStatusOfOrder status;
    private Long totalSumCheck;
    private Long dataOfCreate;

}
