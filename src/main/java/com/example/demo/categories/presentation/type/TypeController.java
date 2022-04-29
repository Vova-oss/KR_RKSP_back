package com.example.demo.categories.presentation.type;

import com.example.demo.categories.domain.type.TypeInputBoundary;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "Type")
@RestController
@RequestMapping("/type")
@CrossOrigin("http://localhost:3000")
public class TypeController {


    @Autowired
    TypeInputBoundary typeInputBoundary;


    @ApiOperation(value = "Добавление новогоТипа")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 400, message = "Such Type already exists")
    })
    @PostMapping("/add")
    public void addType(
            @ApiParam(
                    value = "Наименование Типа",
                    example = "{\n\"name\":\"Смартфон\"\n}",
                    required = true
            )
            @RequestBody String typeName,
            HttpServletResponse response,
            HttpServletRequest request)  {
        typeInputBoundary.addType(typeName, request, response);
    }

    @ApiOperation(value = "Получение всех Типов и принадлежащих к ним Брендов")
    @GetMapping("/getAll")
    public List<TypeResponseModel> getAll(){;
        return typeInputBoundary.getAll();
    }


    @ApiOperation(value = "Удаление Типа по :id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "There isn't exist Type with this :id")
    })
    @DeleteMapping("/delete")
    public void deleteType(
            @ApiParam(
                    value = ":id Типа, который необходимо удалить",
                    example = "{\n\"id\":\"5\"\n}",
                    required = true
            )
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){
        typeInputBoundary.deleteType(body, request, response);
    }


    @ApiOperation(value = "Изменение Типа")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "There isn't exist Type with this :id")
    })
    @PutMapping("/edit")
    public void editType(
            @ApiParam(
                    value = ":id Типа и его новое имя",
                    example = "{\n\"id\":\"5\",\n\"name\":\"Холодильник\"\n}",
                    required = true
            )
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){
        typeInputBoundary.editType(body, request, response);
    }


}
