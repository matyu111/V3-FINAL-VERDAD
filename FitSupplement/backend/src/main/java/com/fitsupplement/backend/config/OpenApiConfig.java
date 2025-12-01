package com.fitsupplement.backend.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("FitSupplement API")
                .description("REST API para la plataforma FitSupplement")
                .version("v1")
                .contact(new Contact().name("Equipo FitSupplement").email("support@fitsupplement.local"))
                .license(new License().name("Proprietary").url("https://fitsupplement.local"))
            );
    }
}