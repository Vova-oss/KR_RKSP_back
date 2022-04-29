package com.example.demo.categories.domain.brand;

import com.example.demo.controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.categories.data.brand.BrandGateway;
import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.device.DeviceInputBoundary;
import com.example.demo.categories.domain.type.TypeEntity;
import com.example.demo.categories.domain.type.TypeInputBoundary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Component
public class BrandInteractor implements BrandInputBoundary {

    @Autowired
    BrandGateway brandGateway;

    @Autowired
    DeviceInputBoundary deviceInputBoundary;

    @Autowired
    TypeInputBoundary typeInputBoundary;

    /**
     * Добавление нового Бренда
     * @param body [json] название Типа(type),
     *             которому принадлежит Бренд, и название нового Бренда(name)
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - Such Brand of this Type already exist
     * @code 400 - Such Type doesn't exist
     */
    @Override
    public void addBrand(String body, HttpServletRequest request, HttpServletResponse response) {
        TypeEntity typeEntity = typeInputBoundary.findByName(StaticMethods.parsingJson(body, "type", request, response));

        if(typeEntity!=null){
            BrandEntity brandEntity = null;
            try {
                brandEntity = new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .readValue(body, BrandEntity.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if(brandEntity == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect JSON");

            List<Long> listBrandId = typeEntity.getId_brands();
            List<BrandEntity> list = new LinkedList<>();
            for(Long id: listBrandId)
                list.add(brandGateway.findById(id));

            for(BrandEntity currentBrandEntity: list){
                if(currentBrandEntity.getName().equals(brandEntity.getName()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Such Brand of this Type already exist");
            }

            brandEntity.setTypeEntity(typeEntity.getName());
            brandGateway.save(brandEntity);
            StaticMethods.createResponse(request,response,HttpServletResponse.SC_CREATED, "Created");
            return;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Such Type doesn't exist");
    }


    /**
     * Получение Бренда по его имени и Типу, к которому он принадлежит
     * @param brandName наименование Бренда
     * @param typeEntity Тип, к которому принадлежит Бренд
     * @return Надейнный Бренд
     */
    @Override
    public BrandEntity findByNameAndTypeId(String brandName, TypeEntity typeEntity) {
        return brandGateway.findByNameAndTypeId(brandName, typeEntity);
    }

    /**
     * Получения Бренда по его :id
     * @param id - :id Бренда
     */
    @Override
    public BrandEntity findById(Long id) {
        return brandGateway.findById(id);
    }


    /**
     * Удаление Бренда по :id
     * @param body [json] :id Бренда, который необходимо удалить
     * @code 204 - No Content
     * @code 400 - Incorrect JSON
     * @code 400 - There isn't exist Brand with this id
     */
    @Override
    public void deleteBrand(String body, HttpServletRequest request, HttpServletResponse response) {
        String id = StaticMethods.parsingJson(body, "id", request, response);
        if(id == null)
            return;
        BrandEntity brandEntity = brandGateway.findById(Long.valueOf(id));
        if(brandEntity!=null) {

            // Поиск всех девайсов для удаления картинок из static/images
            List<DeviceEntity> list = deviceInputBoundary.findAllByBrandId(brandEntity);
            for (DeviceEntity deviceEntity: list){
                if(deviceEntity.getIsName())
                    new File(System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/images/" + deviceEntity.getPathFile()).delete();
            }


            brandGateway.delete(brandEntity);
            StaticMethods.createResponse(request, response, HttpServletResponse.SC_NO_CONTENT, "No Content");
            return;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There isn't exist Brand with this id");
    }


    /**
     * Изменение существующего Бренда по :id
     *
     * @param body [json] :id Бренда и его новое наименование (name)
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - There isn't exist Brand with this id
     */
    @Override
    public void editBrand(String body, HttpServletRequest request, HttpServletResponse response){
        String id = StaticMethods.parsingJson(body, "id", request, response);
        BrandEntity brandEntity = brandGateway.findById(Long.valueOf(id));
        if(brandEntity!=null){
            brandEntity.setName(StaticMethods.parsingJson(body, "name", request, response));
            brandGateway.save(brandEntity);
            StaticMethods.createResponse(request, response, HttpServletResponse.SC_CREATED, "Created");
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There isn't exist Brand with this id");
    }

}
