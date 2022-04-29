package com.example.demo.categories.domain.order;

import com.example.demo.controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.categories.data.order.OrderGateway;
import com.example.demo.categories.domain.device.DeviceEntity;
import com.example.demo.categories.domain.device.DeviceInputBoundary;
import com.example.demo.categories.domain.jwt_token.JwtTokenInputBoundary;
import com.example.demo.categories.domain.order_device.Order_deviceInputBoundary;
import com.example.demo.categories.domain.user.UserEntity;
import com.example.demo.categories.domain.user.UserInputBoundary;
import com.example.demo.categories.presentation.order.OrderPresenter;
import com.example.demo.categories.presentation.order.OrderResponseModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.example.demo.security.SecurityConstants.HEADER_JWT_STRING;
import static com.example.demo.security.SecurityConstants.TOKEN_PREFIX;

@Component
public class OrderInteractor implements OrderInputBoundary{

    @Autowired
    OrderGateway orderGateway;

    @Autowired
    JwtTokenInputBoundary jwtTokenInputBoundary;
    @Autowired
    UserInputBoundary userInputBoundary;
    @Autowired
    DeviceInputBoundary deviceInputBoundary;
    @Autowired
    Order_deviceInputBoundary order_deviceInputBoundary;

    @Autowired
    OrderPresenter orderPresenter;

    /**
     * Добавление нового Заказа
     * @param body [json] содержит:
     *             totalSumCheck - общая сумма в заказе
     *             orderItems - массив Девайсов в заказе. Один элемент массива содержит:
     *                  id - :id Девайса
     *                  amount - их количество
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     */
    @Override
    public void addAnOrder(String body, HttpServletRequest request, HttpServletResponse response){

        //Получение telephoneNumber текущего пользователя по токену
        String telephoneNumber = jwtTokenInputBoundary.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity user = userInputBoundary.findByTelephoneNumber(telephoneNumber);

        try {

            JSONObject object = new JSONObject(body);
            JSONArray array = object.getJSONArray("orderItems");
            Long totalSumCheck = object.getLong("totalSumCheck");

            OrderEntity orderEntity = createNewOrder(request, response, user, totalSumCheck);
            if(orderEntity == null)
                return;

            for(int i = 0; i< array.length(); i++){

                JSONObject current = array.getJSONObject(i);
                DeviceEntity deviceEntity = deviceInputBoundary.findById(Long.valueOf(current.getString("id")));
                if(deviceEntity==null){
                    continue;
                }
                order_deviceInputBoundary.save(orderEntity, deviceEntity, current.getLong("amount"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect JSON");
        }
        StaticMethods.createResponse(request, response, 201, "Created");
    }


    /**
     * Создание новой записи о Заказе в БД
     * @param userEntity Пользователь, которому принадлежит данный Заказ
     * @param totalSumCheck общая сумма Заказа
     * @return созданный Заказ в БД (с :id)
     */
    private OrderEntity createNewOrder(HttpServletRequest request,
                                HttpServletResponse response,
                                UserEntity userEntity,
                                Long totalSumCheck){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId_userEntity(userEntity.getId());
        orderEntity.setStatus(EStatusOfOrder.ACTIVE);
        orderEntity.setTotalSumCheck(totalSumCheck);
        orderEntity.setDataOfCreate(System.currentTimeMillis());

        return orderGateway.save(orderEntity);
    }


    /**
     * Получение всех Заказов (DTO) по Пользователю
     * @param request request, который должен содержать jwToken
     */
    @Override
    public List<OrderResponseModel> getAllOrderResponseModelsByUser(HttpServletRequest request) {

        //Получение telephoneNumber текущего пользователя по токену
        String email = jwtTokenInputBoundary.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity user = userInputBoundary.findByTelephoneNumber(email);

        List<OrderEntity> orders = orderGateway.findAllByUser(user);

        return orderPresenter.successResponse(orders);
    }


    /** Получение абсолютно всех Заказов (DTO) */
    @Override
    public List<OrderResponseModel> getAllOrderResponseModels(){
        List<OrderEntity> orders = orderGateway.findAll();
        return orderPresenter.successResponse(orders);
    }


    /**
     * Изменение Статуса заказа
     * @param body [json] содержит:
     *             id - :id Заказа
     *             status - новый статус Заказа (ACTIVE/INACTIVE)
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - Order with this :id doesn't exist
     * @code 400 - Incorrect status
     */
    @Override
    public void changeStatusOfOrder(String body, HttpServletRequest request, HttpServletResponse response) {

        String id = StaticMethods.parsingJson(body, "id", request, response);
        String status = StaticMethods.parsingJson(body, "status", request, response);
        if(id == null || status == null)
            return;

        OrderEntity orderEntity = orderGateway.findById(Long.valueOf(id));
        if(orderEntity == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order with this :id doesn't exist");

        EStatusOfOrder eStatus;
        try {
            eStatus= EStatusOfOrder.valueOf(status);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect status");
        }

        orderEntity.setStatus(eStatus);
        orderGateway.save(orderEntity);
        StaticMethods.createResponse(request, response, 201, "Created");

    }

}
