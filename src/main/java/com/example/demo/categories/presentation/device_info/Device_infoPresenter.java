package com.example.demo.categories.presentation.device_info;

import com.example.demo.categories.domain.device_info.Device_infoEntity;

import java.util.List;

public interface Device_infoPresenter {

    List<Device_infoResponseModel> successListDevice_infoResponseModel(List<Device_infoEntity> list);

}
