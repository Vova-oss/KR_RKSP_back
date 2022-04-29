package com.example.demo.categories.data.device;

import com.example.demo.categories.data.brand.BrandDataMapper;
import com.example.demo.categories.data.device_info.Device_infoDataMapper;
import com.example.demo.categories.data.order_device.Order_deviceDataMapper;
import com.example.demo.categories.data.rating.RatingDataMapper;
import com.example.demo.categories.data.type.TypeDataMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_device", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class DeviceDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_seq")
    @SequenceGenerator(name = "device_seq", allocationSize = 1, initialValue = 2082)
    @ApiModelProperty(notes = ":id пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Название Девайса", name = "name", required = true, example = "EI540-A Чёрный")
    @Column(name = "name")
    private String name;


    @ApiModelProperty(notes = "Цена Девайса", name = "price", required = true, example = "500")
    @Column(name = "price")
    @PositiveOrZero(message = "Некорректная цена девайса")
    @NotNull(message = "Некорректная цена девайса")
    private String price;


    @ApiModelProperty(notes = "Путь до картинки", name = "pathFile", required = true,
            example = "3e9f88a8-6f12-4683-88c8-c6e6d3efc593fff.png")
    @Column(name = "path_file", columnDefinition = "TEXT")
    private String pathFile;


    @ApiModelProperty(notes = "Поле, отвечающее на вопрос:Является ли pathFile именем картинки?(Если нет, значит эта " +
            "прямая ссылка из инета)", name = "isName", required = true, example = "true")
    @Column(name = "is_name")
    private Boolean isName;


    @ApiModelProperty(notes = "Тип, к которому принадлежит данный Девайс", name = "typeId", required = true)
    @ManyToOne
    @JoinColumn(name = "type_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TypeDataMapper typeDataMapper;


    @ApiModelProperty(notes = "Бренд, к которому принадлежит данный Девайс", name = "brandId", required = true)
    @ManyToOne
    @JoinColumn(name = "brand_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BrandDataMapper brandDataMapper;


    @ApiModelProperty(notes = "Рейтинг данного девайса", name = "ratings")
    @OneToMany(mappedBy = "deviceDataMapper")
    private List<RatingDataMapper> ratingDataMappers;


    @ApiModelProperty(notes = "Дополнительная инфа о Девайсе", name = "deviceInfos")
    @OneToMany(mappedBy = "deviceDataMapper", cascade = CascadeType.ALL)
    private List<Device_infoDataMapper> device_infoDataMappers;


    @ApiModelProperty(notes = "Строки заказов, к которым принадлежит данный Девайс", name = "order_device")
    @OneToOne(mappedBy = "deviceDataMapper")
    private Order_deviceDataMapper order_deviceDataMapper;


    @ApiModelProperty(notes = "Дата создания Девайса", name = "dataOfCreate", required = true)
    @Column(name = "data_of_create")
    @DateTimeFormat
    private Long dataOfCreate;

}
