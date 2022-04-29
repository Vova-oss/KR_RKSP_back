package com.example.demo.categories.data.order;

import com.example.demo.categories.data.order_device.Order_deviceDataMapper;
import com.example.demo.categories.data.user.UserDataMapper;
import com.example.demo.categories.domain.order.EStatusOfOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_order")
public class OrderDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = ":id пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Пользователь, к которому относится заказ", name = "user", required = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserDataMapper userDataMapper;


    @ApiModelProperty(notes = "Ссылки на order-devices", name = "order_devices", required = true)
    @OneToMany(mappedBy = "orderDataMapper")
    private List<Order_deviceDataMapper> order_deviceDataMappers;


    @ApiModelProperty(notes = "Статус заказа (ACTIVE/INACTIVE)", name = "status", required = true, example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private EStatusOfOrder status;


    @ApiModelProperty(notes = "Общая сумма заказа", name = "totalSumCheck", required = true, example = "1950")
    @Column(name = "total_sum_check")
    @PositiveOrZero(message = "Некорректная итоговая сумма")
    @NotNull(message = "Некорректная итоговая сумма")
    private Long totalSumCheck;


    @ApiModelProperty(notes = "Дата создания заказа", name = "dataOfCreate", required = true)
    @Column(name = "data_of_create")
    @DateTimeFormat
    private Long dataOfCreate;

}
