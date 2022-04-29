package com.example.demo.categories.domain.order;

import com.example.demo.categories.presentation.order.OrderResponseModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface OrderInputBoundary {


    void addAnOrder(String body, HttpServletRequest request, HttpServletResponse response);

    List<OrderResponseModel> getAllOrderResponseModelsByUser(HttpServletRequest request);

    List<OrderResponseModel> getAllOrderResponseModels();

    void changeStatusOfOrder(String body, HttpServletRequest request, HttpServletResponse response);
}
