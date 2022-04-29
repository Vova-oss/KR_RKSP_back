package com.example.demo.categories.domain.device_info;

import com.example.demo.categories.data.device_info.Device_infoGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class Device_infoInteractor implements Device_infoInputBoundary{

    @Autowired
    Device_infoGateway device_infoGateway;

    /** Добавление новой дополнительной информации о девайсе в БД */
    @Override
    public void addDevice_info(Device_infoEntity device_infoEntity){
        device_infoGateway.save(device_infoEntity);
    }

    /**
     * Получение листа дополнительно информации о девайсе по листу :id
     * @param id_device_infoEntities - лист айдишников
     */
    @Override
    public List<Device_infoEntity> findByListId(List<Long> id_device_infoEntities) {
        List<Device_infoEntity> list = new LinkedList<>();
        for(Long id: id_device_infoEntities)
            list.add(device_infoGateway.findById(id));

        return list;
    }
}
