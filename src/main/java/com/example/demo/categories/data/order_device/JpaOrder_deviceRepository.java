package com.example.demo.categories.data.order_device;

import com.example.demo.categories.data.order.OrderDataMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOrder_deviceRepository extends JpaRepository<Order_deviceDataMapper, Long> {
    List<Order_deviceDataMapper> findAllByOrderDataMapper(OrderDataMapper orderDataMapper);
}
