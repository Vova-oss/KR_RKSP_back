package com.example.demo.categories.data.brand;

import com.example.demo.categories.data.device.DeviceDataMapper;
import com.example.demo.categories.data.type.TypeDataMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_brand")
public class BrandDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_seq")
    @SequenceGenerator(name = "brand_seq", initialValue = 214, allocationSize = 1)
    @ApiModelProperty(notes = ":id Бренда", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Название Бренда", name = "name", required = true, example = "Apple")
    @Column(name = "name")
    private String name;


    @ApiModelProperty(notes = "Девайсы, которые принадлежат данному бренду", name = "devices")
    @JsonIgnore
    @OneToMany(mappedBy = "brandDataMapper")
    private List<DeviceDataMapper> deviceDataMappers;


    @ApiModelProperty(notes = "Тип, к которому принадлежит данный Бренд", name = "typeId", required = true)
    @ManyToOne()
    @JoinColumn(name = "type_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TypeDataMapper typeDataMapper;

}
