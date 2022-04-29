package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.InputStream;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .tags(
                        new Tag("User", "Взаимодействие с Пользователями"),
                        new Tag("Brand", "Взаимодействие с Брендами"),
                        new Tag("Create DB", "Парсинг данных для автоматического заполнения БД"),
                        new Tag("Security", "Безопасная составляющая"),
                        new Tag("Sending Images", "Класс для отправки картинок"),
                        new Tag("Type", "Взаимодействие с Типами"),
                        new Tag("Device", "Взаимодействие с Девайсами"),
                        new Tag("Order", "Взаимодействие с Заказами")
                )
                .ignoredParameterTypes(Resource.class, InputStream.class);


    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Документация по API приложения OnlineShop")
                .description("Здесь вся информация, необходимая для понимания работы данного REST-API приложения")
                .version("V0.0.0")
                .build();
    }
}
