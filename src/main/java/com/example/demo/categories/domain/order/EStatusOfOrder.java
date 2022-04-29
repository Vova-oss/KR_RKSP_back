package com.example.demo.categories.domain.order;

public enum EStatusOfOrder {

    ACTIVE("Активный"), INACTIVE("Неактивный");
    String status;

    EStatusOfOrder(String status){
        this.status = status;
    }
}
