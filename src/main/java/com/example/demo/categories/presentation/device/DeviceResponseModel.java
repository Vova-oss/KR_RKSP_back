package com.example.demo.categories.presentation.device;

import com.example.demo.categories.presentation.device_info.Device_infoResponseModel;
import lombok.Data;

import java.util.List;

@Data
public class DeviceResponseModel {


    private Long id;
    private String name;
    private String price;
    private String pathFile;
    private Boolean isName;
    private String typeName;
    private String brandName;
    private double ratings;
    private List<Device_infoResponseModel> device_infoResponseModels;


}
