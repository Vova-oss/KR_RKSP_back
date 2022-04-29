package com.example.demo.categories.presentation.type;

import com.example.demo.categories.domain.type.TypeEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface TypePresenter {
    void prepareSuccessView(HttpServletRequest request,
                            HttpServletResponse response,
                            int status,
                            String info);

    List<TypeResponseModel> prepareSuccessView(List<TypeEntity> list);

}
