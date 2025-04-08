package com.example.bankingapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankingAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking Application API")
                        .version("1.0")
                        .description("Professional Banking Application for GL, Transfers, and User Management")
                );
    }
}
