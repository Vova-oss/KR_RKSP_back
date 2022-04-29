package com.example.demo.categories.domain.type;

import com.example.demo.controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.categories.data.type.TypeGateway;
import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.device.DeviceInputBoundary;
import com.example.demo.categories.presentation.type.TypePresenter;
import com.example.demo.categories.presentation.type.TypeResponseModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@Component
public class TypeInteractor implements TypeInputBoundary {

    @Autowired
    TypeGateway typeGateway;

    @Autowired
    DeviceInputBoundary deviceInputBoundary;

    @Autowired
    TypePresenter typePresenter;

    /**
     * Добавление Типа
     * @param name название Типа
     * @code 201 - Created
     * @code 400 - Such Type already exists
     */
    @Override
    public void addType(String name, HttpServletRequest request, HttpServletResponse response){
        TypeEntity typeEntity = null;
        try {
            typeEntity = new ObjectMapper().readValue(name, TypeEntity.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if(!typeGateway.existsByName(typeEntity.getName())){
            typeGateway.save(typeEntity);
            typePresenter.prepareSuccessView(request, response, HttpServletResponse.SC_CREATED, "Created");
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Such Type already exists");
    }


    /** Получение абсолютно всех Типов */
    @Override
    public List<TypeResponseModel> getAll() {
        return typePresenter.prepareSuccessView(typeGateway.findAll());
    }


    /**
     * Получение Типа по его названию
     * @param typeName название Типа
     */
    @Override
    public TypeEntity findByName(String typeName) {
        return typeGateway.findByName(typeName);
    }


    /**
     * Получение Типа по его :id
     * @param id_typeEntity - :id Типа
     */
    @Override
    public TypeEntity findById(Long id_typeEntity) {
        return typeGateway.findById(id_typeEntity);
    }


    /**
     * Удаление Типа по его :id
     * @param body [json] содержит :id Типа
     * @code 204 - No Content
     * @code 400 - Incorrect JSON
     * @code 400 - There isn't exist Type with this :id
     */
    @Override
    public void deleteType(String body, HttpServletRequest request, HttpServletResponse response) {

        String id = StaticMethods.parsingJson(body, "id", request, response);
        if(id == null)
            return;
        TypeEntity typeEntity = typeGateway.findById(Long.valueOf(id));

        if(typeEntity!=null) {

            // Поиск всех девайсов для удаления картинок из static/images
            List<DeviceEntity> list = deviceInputBoundary.findAllByTypeId(typeEntity);
            for(DeviceEntity deviceEntity: list){
                if(deviceEntity.getIsName())
                    new File(System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/images/" + deviceEntity.getPathFile()).delete();
            }

            typeGateway.delete(typeEntity);
            StaticMethods.createResponse(request, response, HttpServletResponse.SC_NO_CONTENT, "No Content");

        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There isn't exist Type with this :id");
    }


    /**
     * Изменение Типа по его :id
     * @param body [json] содержит:
     *             id - :id Типа, который желаем изменить
     *             name - новое название Типа
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 There isn't exist Type with this :id
     */
    @Override
    public void editType(String body, HttpServletRequest request, HttpServletResponse response) {

        String id = StaticMethods.parsingJson(body, "id", request, response);
        if(id == null)
            return;
        TypeEntity typeEntity = typeGateway.findById(Long.valueOf(id));

        if(typeEntity != null){
            String name = StaticMethods.parsingJson(body,"name", request, response);
            typeEntity.setName(name);
            typeGateway.save(typeEntity);
            StaticMethods.createResponse(request, response, HttpServletResponse.SC_CREATED, "Created");
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There isn't exist Type with this :id");
    }



}
