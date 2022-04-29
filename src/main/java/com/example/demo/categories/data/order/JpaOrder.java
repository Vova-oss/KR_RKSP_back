package com.example.demo.categories.data.order;

import com.example.demo.categories.data.order_device.Order_deviceDataMapper;
import com.example.demo.categories.data.order_device.Order_deviceGateway;
import com.example.demo.categories.data.user.UserGateway;
import com.example.demo.categories.domain.order.OrderEntity;
import com.example.demo.categories.domain.order_device.Order_deviceEntity;
import com.example.demo.categories.domain.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JpaOrder implements OrderGateway{

    @Autowired
    JpaOrderRepository jpaOrderRepository;

    @Autowired
    UserGateway userGateway;
    @Autowired
    Order_deviceGateway order_deviceGateway;

    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        OrderDataMapper orderDataMapper = createOrderDataMapper(orderEntity);
        orderDataMapper = jpaOrderRepository.save(orderDataMapper);
        return createOrderEntity(orderDataMapper);
    }

    @Override
    public List<OrderEntity> findAllByUser(UserEntity userEntity) {
        List<OrderDataMapper> list = jpaOrderRepository.findAllByUserDataMapper(userGateway.createUserDataMapper(userEntity));
        return createListOrderEntity(list);
    }

    @Override
    public List<OrderEntity> findAll() {
        List<OrderDataMapper> list = jpaOrderRepository.findAll();
        return createListOrderEntity(list);
    }

    @Override
    public OrderEntity findById(Long id) {
        OrderDataMapper orderDataMapper = jpaOrderRepository.findById(id).orElse(null);
        return orderDataMapper == null? null : createOrderEntity(orderDataMapper);
    }

    @Override
    public OrderEntity createOrderEntity(OrderDataMapper orderDataMapper){
        if(orderDataMapper == null)
            return null;

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderDataMapper.getId());
        orderEntity.setStatus(orderDataMapper.getStatus());
        orderEntity.setTotalSumCheck(orderDataMapper.getTotalSumCheck());
        orderEntity.setDataOfCreate(orderDataMapper.getDataOfCreate());
        orderEntity.setId_userEntity(orderDataMapper.getUserDataMapper().getId());
        List<Long> listId = null;
        if(orderDataMapper.getOrder_deviceDataMappers()!=null){
            listId =orderDataMapper.getOrder_deviceDataMappers()
                    .stream().map(Order_deviceDataMapper::getId).collect(Collectors.toList());
        }
        orderEntity.setId_order_deviceEntities(listId);
        return orderEntity;
    }

    @Override
    public OrderDataMapper createOrderDataMapper(OrderEntity orderEntity){
        if(orderEntity == null)
            return null;

        OrderDataMapper orderDataMapper = new OrderDataMapper();
        orderDataMapper.setId(orderEntity.getId());
        orderDataMapper.setTotalSumCheck(orderEntity.getTotalSumCheck());
        orderDataMapper.setDataOfCreate(orderEntity.getDataOfCreate());
        orderDataMapper.setStatus(orderEntity.getStatus());
        UserEntity userEntity = userGateway.findById(orderEntity.getId_userEntity());
        orderDataMapper.setUserDataMapper(userGateway.createUserDataMapper(userEntity));
        List<Order_deviceEntity> list = order_deviceGateway.findByListId(orderEntity.getId_order_deviceEntities());
        orderDataMapper.setOrder_deviceDataMappers(order_deviceGateway.createListOrder_deviceDataMapper(list));

        return orderDataMapper;
    }

    @Override
    public List<OrderEntity> createListOrderEntity(List<OrderDataMapper> list){
        if(list == null)
            return null;


        List<OrderEntity> newList = new LinkedList<>();
        for(OrderDataMapper orderDataMapper: list)
            newList.add(createOrderEntity(orderDataMapper));

        return newList;
    }

    @Override
    public List<OrderDataMapper> createListOrderDataMapper(List<OrderEntity> list){
        if(list == null)
            return null;

        List<OrderDataMapper> newList = new LinkedList<>();
        for(OrderEntity orderEntity: list)
            newList.add(createOrderDataMapper(orderEntity));

        return newList;
    }
}
