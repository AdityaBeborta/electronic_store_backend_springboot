package com.electronicstore.swaggerconfigs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
//Swagger UI customizations
@SecurityScheme(
        name = "scheme default",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        info = @Info(
                title = "electronic store backend application with spring boot, spring security + JWT",
                description = "spring boot REST based APIs for shopping applications which can be consumed",
                version = "V1",
                contact = @Contact(
                        name = "Aditya",
                        email = "adi"
                )
        )
)
//spring security configuration in swagger

public class SwaggerConfig {

    //no longer need as we are doing the config by using the annotations
//    @Bean
//    public OpenAPI customizeSwaggerConfig() {
//        OpenAPI openAPI = new OpenAPI();
//        openAPI.setInfo(new Info().title("electronic store backend application with spring boot, spring security + JWT")
//                .description("This is a backend shopping application REST based architecture")
//                .termsOfService("copyright")
//                .version("V1")
//                .contact(new Contact().email("adi").name("aditya")));
//        return openAPI;
//
//    }

}
