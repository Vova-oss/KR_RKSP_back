package com.example.demo.categories.data.order_device;

import com.example.demo.categories.data.device.DeviceGateway;
import com.example.demo.categories.data.order.OrderGateway;
import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.order.OrderEntity;
import com.example.demo.categories.domain.order_device.Order_deviceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class JpaOrder_device implements Order_deviceGateway{

    @Autowired
    JpaOrder_deviceRepository jpaOrder_deviceRepository;

    @Autowired
    DeviceGateway deviceGateway;
    @Autowired
    OrderGateway orderGateway;

    @Override
    public void save(OrderEntity orderEntity, DeviceEntity deviceEntity, long amount) {
        Order_deviceDataMapper order_deviceDataMapper = new Order_deviceDataMapper();
        order_deviceDataMapper.setAmountOfProduct(amount);
        order_deviceDataMapper.setDeviceDataMapper(deviceGateway.createDeviceDataMapper(deviceEntity));
        order_deviceDataMapper.setOrderDataMapper(orderGateway.createOrderDataMapper(orderEntity));
        jpaOrder_deviceRepository.save(order_deviceDataMapper);
    }

    @Override
    public Order_deviceEntity findById(Long id) {
        Order_deviceDataMapper order_deviceDataMapper = jpaOrder_deviceRepository.findById(id).orElse(null);
        return order_deviceDataMapper == null ? null : createOrder_deviceEntity(order_deviceDataMapper);
    }

    @Override
    public List<Order_deviceEntity> findByListId(List<Long> id_order_deviceEntities) {
        if(id_order_deviceEntities == null)
            return null;
        List<Order_deviceEntity> list = new LinkedList<>();
        for(Long id: id_order_deviceEntities)
            list.add(findById(id));

        return list;
    }

    @Override
    public Order_deviceDataMapper createOrder_deviceDataMapper (Order_deviceEntity order_deviceEntity){
        if(order_deviceEntity == null)
            return null;
        Order_deviceDataMapper order_deviceDataMapper = new Order_deviceDataMapper();
        order_deviceDataMapper.setId(order_deviceEntity.getId());
        order_deviceDataMapper.setAmountOfProduct(order_deviceEntity.getAmountOfProduct());

        DeviceEntity deviceEntity = deviceGateway.findById(order_deviceEntity.getId_deviceEntity());
        order_deviceDataMapper.setDeviceDataMapper(deviceGateway.createDeviceDataMapper(deviceEntity));
        return order_deviceDataMapper;
    }

    @Override
    public Order_deviceEntity createOrder_deviceEntity(Order_deviceDataMapper order_deviceDataMapper){
        if(order_deviceDataMapper == null)
            return null;
        Order_deviceEntity order_deviceEntity = new Order_deviceEntity();
        order_deviceEntity.setId(order_deviceDataMapper.getId());
        order_deviceEntity.setAmountOfProduct(order_deviceDataMapper.getAmountOfProduct());
        order_deviceEntity.setId_deviceEntity(order_deviceDataMapper.getDeviceDataMapper().getId());
        return order_deviceEntity;
    }

    @Override
    public List<Order_deviceEntity> createListOrder_deviceEntities(List<Order_deviceDataMapper> list) {
        if(list == null)
            return null;
        List<Order_deviceEntity> newList = new LinkedList<>();
        for(Order_deviceDataMapper order_deviceDataMapper: list)
            newList.add(createOrder_deviceEntity(order_deviceDataMapper));

        return newList;
    }

    @Override
    public List<Order_deviceDataMapper> createListOrder_deviceDataMapper(List<Order_deviceEntity> list) {
        if(list == null)
            return null;
        List<Order_deviceDataMapper> newList = new LinkedList<>();
        for(Order_deviceEntity order_deviceEntity: list)
            newList.add(createOrder_deviceDataMapper(order_deviceEntity));

        return newList;
    }
}
