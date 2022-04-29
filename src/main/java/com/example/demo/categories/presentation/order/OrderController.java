package com.example.demo.categories.presentation.order;

import com.example.demo.categories.domain.order.OrderInputBoundary;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "Order")
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderInputBoundary orderInputBoundary;

    @ApiOperation(value = "Добавление нового заказа")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 400, message = "Incorrect JSON\n" +
                    "There isn't exist Device with this id")
    })
    @PostMapping("/add")
    public void addAnOrder(
            @ApiParam(
                    value = ":id и :amount Девайсов",
                    example = "{\n\"totalSumCheck\":\"150\",\n\"orderItems\":" +
                            "[{\n\"id\":\"20\",\n\"amount\":\"3\"\n}\n,...\n]}",
                    required = true
            )
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){

        orderInputBoundary.addAnOrder(body, request, response);
    }



    @ApiOperation(value = "Получение всех заказов конкретного пользователя")
    @GetMapping("/getAllByUser")
    public List<OrderResponseModel> getAllOrdersByUser(HttpServletRequest request){

        return orderInputBoundary.getAllOrderResponseModelsByUser(request);

    }



    @ApiOperation(value = "Получение абсолютно всех заказов")
    @GetMapping("/getAll")
    public List<OrderResponseModel> getAllOrders(){
        return orderInputBoundary.getAllOrderResponseModels();
    }



    @ApiOperation(value = "Изменение статуса у заказа")
    @PutMapping("/changeStatusOfOrder")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Order with this :id doesn't exist\n" +
                    "Incorrect status")
    })
    public void changeStatusOfOrder(
            @ApiParam(
                    value = ":id Заказа и новый статус (ACTIVE/INACTIVE)",
                    example = "{\n\"id\":\"5\",\n\"status\":\"ACTIVE\"\n}",
                    required = true
            )
            @RequestBody String body, HttpServletRequest request, HttpServletResponse response){
        orderInputBoundary.changeStatusOfOrder(body, request, response);
    }

}
