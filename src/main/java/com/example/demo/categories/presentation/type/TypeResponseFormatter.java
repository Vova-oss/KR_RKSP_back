package com.example.demo.categories.presentation.type;

import com.example.demo.categories.domain.type.TypeEntity;
import com.example.demo.categories.presentation.brand.BrandPresenter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class TypeResponseFormatter implements TypePresenter {

    @Autowired
    BrandPresenter brandPresenter;

    @Override
    public void prepareSuccessView(HttpServletRequest request, HttpServletResponse response, int status, String info) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", status);
        body.put("info", info);
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(response.getOutputStream(), body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TypeResponseModel> prepareSuccessView(List<TypeEntity> list) {

        List<TypeResponseModel> newList = new LinkedList<>();
        for(TypeEntity typeEntity: list){
            TypeResponseModel typeResponseModel = new TypeResponseModel();
            typeResponseModel.setId(typeEntity.getId());
            typeResponseModel.setName(typeEntity.getName());
            typeResponseModel.setBrands(brandPresenter.createListBrandResponseModel(typeEntity.getId_brands()));
            newList.add(typeResponseModel);
        }
        return newList;
    }


}
