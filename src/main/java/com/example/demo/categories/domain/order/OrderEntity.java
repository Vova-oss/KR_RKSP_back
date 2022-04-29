package com.example.demo.categories.domain.order;

import com.example.demo.categories.domain.order_device.Order_deviceEntity;
import com.example.demo.categories.domain.user.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class OrderEntity {

    private Long id;
    private Long id_userEntity;
    private List<Long> id_order_deviceEntities;
    private EStatusOfOrder status;
    private Long totalSumCheck;
    private Long dataOfCreate;

}
