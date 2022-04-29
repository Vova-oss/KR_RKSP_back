package com.example.demo.categories.data.order;

import com.example.demo.categories.data.user.UserDataMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOrderRepository extends JpaRepository<OrderDataMapper, Long> {

    List<OrderDataMapper> findAllByUserDataMapper(UserDataMapper userDataMapper);

}
