package com.example.demo.categories.data.device_info;

import com.example.demo.categories.data.device.DeviceDataMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_device_info")
public class Device_infoDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_info_seq")
    @SequenceGenerator(name = "device_info_seq", initialValue = 14398, allocationSize = 1)
    @ApiModelProperty(notes = ":id пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Заголовок", name = "title", required = true, example = "Ширина")
    @Column(name = "title")
    private String title;


    @ApiModelProperty(notes = "Описание", name = "description", required = true, example = "2 метра")
    @Column(name = "description")
    private String description;


    @ApiModelProperty(notes = "Девайс, к которому идёт описание", name = "device", required = true)
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "device_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DeviceDataMapper deviceDataMapper;

}
