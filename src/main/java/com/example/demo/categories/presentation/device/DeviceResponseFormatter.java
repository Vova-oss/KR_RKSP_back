package com.example.demo.categories.presentation.device;

import com.example.demo.categories.domain.brand.BrandInputBoundary;
import com.example.demo.categories.domain.brand.BrandInteractor;
import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.device_info.Device_infoEntity;
import com.example.demo.categories.domain.device_info.Device_infoInputBoundary;
import com.example.demo.categories.domain.type.TypeInputBoundary;
import com.example.demo.categories.presentation.device_info.Device_infoPresenter;
import com.example.demo.categories.presentation.device_info.Device_infoResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class DeviceResponseFormatter implements DevicePresenter{

    @Autowired
    Device_infoPresenter device_infoPresenter;

    @Autowired
    Device_infoInputBoundary device_infoInputBoundary;
    @Autowired
    BrandInputBoundary brandInputBoundary;
    @Autowired
    TypeInputBoundary typeInputBoundary;

    @Override
    public List<DeviceResponseModel> createListDeviceResponseModel(List<DeviceEntity> list) {
        if(list == null)
            return null;

        List<DeviceResponseModel> newList = new LinkedList<>();
        for(DeviceEntity deviceEntity: list)
            newList.add(createDeviceResponseModel(deviceEntity));
        return newList;

    }

    @Override
    public DeviceResponseModel createDeviceResponseModel(DeviceEntity deviceEntity) {
        DeviceResponseModel deviceResponseModel = new DeviceResponseModel();

        deviceResponseModel.setId(deviceEntity.getId());
        deviceResponseModel.setName(deviceEntity.getName());
        deviceResponseModel.setPrice(deviceEntity.getPrice());
        deviceResponseModel.setPathFile(deviceEntity.getPathFile());
        deviceResponseModel.setIsName(deviceEntity.getIsName());

        deviceResponseModel.setTypeName(typeInputBoundary.findById(deviceEntity.getId_typeEntity()).getName());
        deviceResponseModel.setBrandName(brandInputBoundary.findById(deviceEntity.getId_brandEntity()).getName());
        deviceResponseModel.setRatings(createRating(deviceEntity));
        List<Device_infoEntity> list = device_infoInputBoundary.findByListId(deviceEntity.getId_device_infoEntities());
        List<Device_infoResponseModel> responseModels = device_infoPresenter.successListDevice_infoResponseModel(list);
        deviceResponseModel.setDevice_infoResponseModels(responseModels);

        return deviceResponseModel;
    }

    private double createRating(DeviceEntity deviceEntity){

        double commonRating = 0;
//        for(Rating rating :deviceEntity.getRatings()){
//            commonRating += Double.parseDouble(rating.getRate());
//        }
        return commonRating;
    }

    @Override
    public DeviceWithParamsResponseModel successDeviceWithParamsResponseModel(List<DeviceEntity> list, int amountOfAllDevices, String maxPrice, String minPrice) {
        List<DeviceResponseModel> newList = new LinkedList<>();
        for(DeviceEntity deviceEntity: list)
            newList.add(createDeviceResponseModel(deviceEntity));

        DeviceWithParamsResponseModel deviceWithParamsResponseModel = new DeviceWithParamsResponseModel();
        deviceWithParamsResponseModel.setList(newList);
        deviceWithParamsResponseModel.setAmountOfAllDevices(amountOfAllDevices);
        deviceWithParamsResponseModel.setMaxPrice(maxPrice);
        deviceWithParamsResponseModel.setMinPrice(minPrice);
        return deviceWithParamsResponseModel;
    }


}
