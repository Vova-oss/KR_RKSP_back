package com.example.demo.controller.AuxiliaryClasses;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StaticMethods {


    /**
     * Создание ответа
     * @param status - статус ответа
     * @param info - инфорация, которая будет прописана под полем "info"
     */
    public static void createResponse(HttpServletRequest request,
                                      HttpServletResponse response,
                                      int status,
                                      String info){
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);
//        response.addHeader("Access-Control-Allow-Origin", "*" );

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


    /**
     * Создание ответа для передачи всех ошибок(данных), которые были обнаружены при валидации
     * @param field лист полей, к которым относятся ошибки
     * @param info характеристика каждой ошибки
     *
     * @code 469 - Incorrect validation
     */
    public static void createBadResponseDueToValidation(HttpServletRequest request,
                                      HttpServletResponse response,
                                      List<String> field,
                                      List<String> info){
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(469);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", 469);
        body.put("field", field);
        body.put("info", info);
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(response.getOutputStream(), body);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Получение конкретного поля из json
     * @param body - изначальный json
     * @param field - поле, которое необходимо получить из этого json
     * @return field/null
     *
     * @code 400 - Incorrect JSON
     */
    public static String parsingJson (String body,
                                      String field,
                                      HttpServletRequest request ,
                                      HttpServletResponse response) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            field = jsonObject.getString(field);
        } catch (JSONException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect JSON");
        }
        return field;
    }


    /** метод определения расширения файла */
    public static String getFileExtension(String fileName) {
        // если в имени файла есть точка и она не является первым символом в названии файла
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
            return fileName.substring(fileName.lastIndexOf(".")+1);
            // в противном случае возвращаем заглушку, то есть расширение не найдено
        else return null;
    }






    // Задумка прикольная, жаль, что я даун
//    public static <T extends BaseEntity> T findById(String body,
//                                                    HttpServletRequest request ,
//                                                    HttpServletResponse response,
//                                                    JpaRepository<T, Long> repository){
//
//        String field = parsingJson(body, "id", request, response);
//        if (field == null)
//            return null;
//
//        List<T> list = repository.findAll();
//        for (T el : list) {
//            if (field.equals(Objects.toString(el.getId()))) {
//                return el;
//            }
//        }
//        return null;
//    }

}
