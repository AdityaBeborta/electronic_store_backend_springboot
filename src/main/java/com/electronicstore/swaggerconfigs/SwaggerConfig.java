package com.electronicstore.swaggerconfigs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customizeSwaggerConfig(){
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(new Info().title("electronic store backend application with spring boot, spring security + JWT")
                .description("This is a backend shopping application REST based architecture")
                .termsOfService("copyright")
                .version("V1")
                .contact(new Contact().email("adi").name("aditya")));
        return openAPI;

    }

}
