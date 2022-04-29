package com.example.demo.categories.domain.device;

import com.example.demo.controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.categories.data.device.DeviceGateway;
import com.example.demo.categories.domain.brand.BrandEntity;
import com.example.demo.categories.domain.brand.BrandInputBoundary;
import com.example.demo.categories.domain.device_info.Device_infoEntity;
import com.example.demo.categories.domain.device_info.Device_infoInputBoundary;
import com.example.demo.categories.domain.type.TypeEntity;
import com.example.demo.categories.domain.type.TypeInputBoundary;
import com.example.demo.categories.presentation.device.DevicePresenter;
import com.example.demo.categories.presentation.device.DeviceResponseModel;
import com.example.demo.categories.presentation.device.DeviceWithParamsResponseModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DeviceInteractor implements DeviceInputBoundary{


    @Autowired
    DeviceGateway deviceGateway;

    @Autowired
    BrandInputBoundary brandInputBoundary;
    @Autowired
    TypeInputBoundary typeInputBoundary;
    @Autowired
    Device_infoInputBoundary device_infoInputBoundary;

    @Autowired
    DevicePresenter devicePresenter;

    /**
     * Добавление Девайса
     *
     * @param brandName название Бренда, к которому будет относится Девайс
     * @param typeName название Типа, к которому будет относится Девайс
     * @param file картинка в битовом представление (ава Девайса)
     * @param ref ссылка на картинку (ава Девайса)
     * @param name название Девайса
     * @param price цена Девайса
     * @param list лист с характеристиками Девайса (Device_info)
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - This name of Device already exists
     * @code 400 - This Type doesn't exist
     * @code 400 - This Brand (%s) of this Type (%s) doesn't exist
     * @code 400 - Incorrect image extension
     */
    @Override
    public void addDevice(String brandName,
                          String typeName,
                          MultipartFile file,
                          String ref,
                          String name,
                          String price,
                          JSONArray list,
                          HttpServletRequest request,
                          HttpServletResponse response) {

        DeviceEntity deviceEntity = new DeviceEntity();

        deviceEntity = createDeviceAndSaveInDB(deviceEntity, brandName, typeName, file, ref, name, price, request, response);
        createDeviceInfoAndSaveInDB(list, deviceEntity, request, response);
    }

    /**
     * Сохранения Девайса в БД с добавлением ему переданных значений
     * @param deviceEntity Девайс, которому добавляют все значения
     * @param brandName название Бренда, к которому будет относится Девайс
     * @param typeName название Типа, к которому будет относится Девайс
     * @param file картинка в битовом представление (ава Девайса)
     * @param ref ссылка на картинку (ава Девайса)
     * @param name название Девайса
     * @param price цена Девайса
     *
     * @code 400 - This name of Device already exists
     * @code 400 - This Type doesn't exist
     * @code 400 - This Brand (%s) of this Type (%s) doesn't exist
     * @code 400 - Incorrect image extension
     */
    private DeviceEntity createDeviceAndSaveInDB(DeviceEntity deviceEntity,
                                        String brandName,
                                        String typeName,
                                        MultipartFile file,
                                        String ref,
                                        String name,
                                        String price,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        if(deviceGateway.existsByName(name)){
            if(deviceEntity.getId() != null) {

                DeviceEntity temp = deviceGateway.findById(deviceEntity.getId());
                if (temp == null || !temp.getName().equals(name))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This name of Device already exists");

            }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This name of Device already exists");
        }

        TypeEntity typeEntity = typeInputBoundary.findByName(typeName);
        if(typeEntity == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This Type doesn't exist");

        BrandEntity brandEntity = brandInputBoundary.findByNameAndTypeId(brandName, typeEntity);
        if(brandEntity == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This Brand of this Type doesn't exist");


        if(file != null) {
            String startName = StaticMethods.getFileExtension(file.getOriginalFilename());
            if(startName==null || (
                    !startName.equals("jpeg")
                            && !startName.equals("jpg")
                            && !startName.equals("pjpeg")
                            && !startName.equals("png")
                            && !startName.equals("tiff")
                            && !startName.equals("wbmp")
                            && !startName.equals("webp"))
            ) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect image extension");

            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + file.getOriginalFilename();
            String pathFile = System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/images/" + fileName;

            try {
                file.transferTo(new File(pathFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            deviceEntity.setPathFile(fileName);
            deviceEntity.setIsName(true);
        }else if(ref != null) {
            deviceEntity.setPathFile(ref);
            deviceEntity.setIsName(false);
        }

        deviceEntity.setId_typeEntity(typeEntity.getId());
        deviceEntity.setId_brandEntity(brandEntity.getId());
        deviceEntity.setName(name);
        deviceEntity.setPrice(price);
        deviceEntity.setDataOfCreate(System.currentTimeMillis());

        return deviceGateway.save(deviceEntity);
    }

    /**
     * Добавление дополнительно информации Девайсу с последующим сохранением в БД
     * @param list лист с характеристиками Девайса (Device_info)
     * @param deviceEntity сам Девайс, которому добавляют Device_info
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     */
    private void createDeviceInfoAndSaveInDB(JSONArray list,
                                            DeviceEntity deviceEntity,
                                            HttpServletRequest request,
                                            HttpServletResponse response){
        try {

            for(int i = 0; i < list.length(); i++){
                String characteristic = list.getString(i);
                Device_infoEntity device_infoEntity = new ObjectMapper().readValue(characteristic, Device_infoEntity.class);
                device_infoEntity.setId_device(deviceEntity.getId());
                device_infoInputBoundary.addDevice_info(device_infoEntity);
            }

            StaticMethods.createResponse(request, response, HttpServletResponse.SC_CREATED, "Created");

        } catch (JSONException | JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect JSON");
        }
    }


    /**
     * Получение Девайсов исходя из входных параметров
     * @param type Название Типа, к которому относится Девайс
     * @param brands Название Бренда, к которому относится Девайс
     * @param page Номер страницы, которую хочет видеть клиент
     * @param limit Количество Девайсов, отображаемых на одной странице у клиента
     * @param minPrice минимальная цена Девайса
     * @param maxPrice максимальная цена Девайса
     *
     * @code = 400 - The minPrice more than the maxPrice
     * @code = 400 - Devices with this type doesn't exists
     * @code = 400 - Devices with this brands doesn't exists
     * @code = 400 - Devices with this price doesn't exists
     * @code = 400 - Incorrect data of page or limit
     */
    @Override
    public DeviceWithParamsResponseModel getByParams(String type,
                                                     List<String> brands,
                                                     int page, int limit,
                                                     int minPrice, int maxPrice,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {

        if(maxPrice!=-1 && minPrice > maxPrice)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The minPrice more than the maxPrice");

        List<DeviceEntity> list = new ArrayList<>();

        // Условие для получения листа Девайсов исходя из переданных Брендов(если они имеются) и Типа
        if(brands==null || brands.size()==0){
            list = deviceGateway.findAllByTypeEntity(typeInputBoundary.findByName(type));

            if(list.isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Devices with this type doesn't exists");

        }else {
            brands = brands.stream().distinct().collect(Collectors.toList());
            for(String brand: brands) {

                TypeEntity typeParent = typeInputBoundary.findByName(type);
                BrandEntity brandParent = brandInputBoundary.findByNameAndTypeId(brand, typeParent);
                list.addAll(deviceGateway.findAllByTypeIdAndBrandId(typeParent, brandParent));
            }

            if(list.isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Devices with this brands doesn't exists");

        }


        // Девайс с максимальной ценой в этом листе
        Optional<DeviceEntity> deviceWithMaxPrice = list.stream().max(Comparator.comparing(o -> Integer.valueOf(o.getPrice())));

        // Девайс с минимальной ценой в этом листе
        Optional<DeviceEntity> deviceWithMinPrice = list.stream().min(Comparator.comparing(o -> Integer.valueOf(o.getPrice())));

        list = selectionByPrice(list, minPrice, maxPrice);

        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Devices with this price doesn't exists");

        // Сортировка по дате создания
        list.sort((o1, o2) -> o2.getDataOfCreate().compareTo(o1.getDataOfCreate()));

        int amountOfAllDevices = list.size();

        // Определение границ эл. в листе исходя из количества отображаемых эл. на стр. и номера стр.
        int fromIndex = (page - 1) * limit;
        int toIndex = page * limit;
        if(fromIndex >= list.size())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect data of page or limit");

        if(toIndex > list.size()){
            toIndex = list.size();
        }

        List<DeviceEntity> listDRM = list.subList(fromIndex, toIndex);

        return devicePresenter.successDeviceWithParamsResponseModel(
                listDRM,
                amountOfAllDevices,
                deviceWithMaxPrice.orElse(null).getPrice(),
                deviceWithMinPrice.orElse(null).getPrice());
    }


    /**
     * Фильтрация листа с Девайсами по цене
     * @param list лист Девайсов, который будет отфильтрован, исходя из мин. и макс. ценовых значений
     * @param minPrice минимальная цена Девайса
     * @param maxPrice максимальная цена Девайса
     */
    private List<DeviceEntity> selectionByPrice(List<DeviceEntity> list, int minPrice, int maxPrice){

        if(maxPrice == -1){

            return list.
                    stream().
                    filter(device -> minPrice <= Integer.parseInt(device.getPrice())).
                    collect(Collectors.toList());
        }else{

            return list.
                    stream().
                    filter(device -> minPrice <= Integer.parseInt(device.getPrice())
                            && Integer.parseInt(device.getPrice()) <= maxPrice).
                    collect(Collectors.toList());
        }
    }


    /**
     * Поиск всех Девайсов по Типу
     * @param typeEntity Тип, к которому принадлежит Девайс
     */
    @Override
    public List<DeviceEntity> findAllByTypeId(TypeEntity typeEntity){
        return deviceGateway.findAllByTypeEntity(typeEntity);
    }


    /**
     * Поиск всех Девайсов по Бренду
     * @param brandEntity Бренд, к которому принадлежит Девайс
     */
    @Override
    public List<DeviceEntity> findAllByBrandId(BrandEntity brandEntity){
        return deviceGateway.findAllByBrandEntity(brandEntity);
    }


    /** Получение всех Девайсов*/
    @Override
    public List<DeviceEntity> getAll() {
        return deviceGateway.findAll();
    }


    /**
     * Удаление Девайса по его :id
     * @param body (json) :id Девайса
     *
     * @code 204 - No Content
     * @code 400 - Incorrect JSON
     * @code 400 - There isn't exist Device with this id
     */
    @Override
    public void deleteDevice(String body, HttpServletRequest request, HttpServletResponse response) {

        String id = StaticMethods.parsingJson(body, "id", request, response);
        if(id == null)
            return;
        DeviceEntity deviceEntity = deviceGateway.findById(Long.valueOf(id));

        if(deviceEntity!=null){
            if(deviceEntity.getIsName())
                new File(System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/images/" + deviceEntity.getPathFile()).delete();

            deviceGateway.delete(deviceEntity);
            StaticMethods.createResponse(request, response,
                    HttpServletResponse.SC_NO_CONTENT, "No Content");
            return;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There isn't exist Device with this id");
    }


    /**
     * Получение Девайса и дальнейшее его конвертирование в DeviceDTO
     * @param id :id Девайса
     * @code 400 - There isn't exist Device with this id
     */
    @Override
    public DeviceResponseModel getDeviceResponseModelById(String id, HttpServletRequest request, HttpServletResponse response) {
        DeviceEntity deviceEntity = deviceGateway.findById(Long.valueOf(id));
        if(deviceEntity==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There isn't exist Device with this id");

        return devicePresenter.createDeviceResponseModel(deviceEntity);
    }


    /**
     * Получение Девайса по :id
     * @param id_deviceEntity :id Девайса
     * @code 400 - There isn't exist Device with this id
     */
    @Override
    public DeviceEntity findById(Long id_deviceEntity) {
        DeviceEntity deviceEntity = deviceGateway.findById(id_deviceEntity);
        if(deviceEntity==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There isn't exist Device with this id");

        return deviceEntity;
    }


    /** Получение топ 24 девайса (сортировка по дате создания (выдаются новые)) */
    @Override
    public List<DeviceResponseModel> getTopDevices() {
        List<DeviceEntity> list = deviceGateway.findAll();
        list.sort((o1, o2) -> o2.getDataOfCreate().compareTo(o1.getDataOfCreate()));
        list = list.subList(0, Math.min(list.size(), 24));
        return devicePresenter.createListDeviceResponseModel(list);
    }


    /**
     * Метод для изменение Девайса по :id
     * @param id :id существующего Девайса, который желаем изменить
     * @param brand название Бренда, к которому будет относится Девайс
     * @param type название Типа, к которому будет относится Девайс
     * @param file картинка в битовом представление (ава Девайса)
     * @param ref ссылка на картинку (ава Девайса)
     * @param name название Девайса
     * @param price цена Девайса
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - This name of Device already exists
     * @code 400 - This Type doesn't exist
     * @code 400 - This Brand (%s) of this Type (%s) doesn't exist
     * @code 400 - Incorrect image extension
     * @code 400 - Device with this :id doesn't exist
     */
    @Override
    public void editDevice(String id,
                           String brand,
                           String type,
                           String ref,
                           MultipartFile file,
                           String name,
                           String price,
                           JSONArray list,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        DeviceEntity deviceEntity = deviceGateway.findById(Long.valueOf(id));
        if(deviceEntity == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Device with this :id doesn't exist");


        createDeviceAndSaveInDB(deviceEntity, brand, type, file, ref, name, price, request, response);
        createDeviceInfoAndSaveInDB(list, deviceEntity, request, response);


    }

}
