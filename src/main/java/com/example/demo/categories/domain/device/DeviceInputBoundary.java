package com.example.demo.categories.domain.device;

import com.example.demo.categories.domain.brand.BrandEntity;
import com.example.demo.categories.domain.type.TypeEntity;
import com.example.demo.categories.presentation.device.DeviceResponseModel;
import com.example.demo.categories.presentation.device.DeviceWithParamsResponseModel;
import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DeviceInputBoundary {
    void addDevice(String brand, String type, MultipartFile file, String ref, String name, String price, JSONArray list, HttpServletRequest request, HttpServletResponse response);

    DeviceWithParamsResponseModel getByParams(String type, List<String> brand, int page, int limit, int minPrice, int maxPrice, HttpServletRequest request, HttpServletResponse response);

    void deleteDevice(String body, HttpServletRequest request, HttpServletResponse response);

    void editDevice(String id, String brand, String type, String ref, MultipartFile file, String name, String price, JSONArray list, HttpServletRequest request, HttpServletResponse response);

    DeviceResponseModel getDeviceResponseModelById(String id, HttpServletRequest request, HttpServletResponse response);

    List<DeviceEntity> getAll();

    List<DeviceEntity> findAllByTypeId(TypeEntity typeEntity);

    List<DeviceEntity> findAllByBrandId(BrandEntity brandEntity);

//    DeviceEntity getById(String id, HttpServletRequest request, HttpServletResponse response);

    DeviceEntity findById(Long id_deviceEntity);

    List<DeviceResponseModel> getTopDevices();
}
