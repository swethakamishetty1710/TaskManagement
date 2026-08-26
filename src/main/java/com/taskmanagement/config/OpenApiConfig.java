package com.taskmanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        SecurityScheme securityScheme =
                new SecurityScheme();

        securityScheme.setType(
                SecurityScheme.Type.HTTP
        );

        securityScheme.setScheme("bearer");

        securityScheme.setBearerFormat("JWT");

        Components components =
                new Components();

        components.addSecuritySchemes(
                "bearerAuth",
                securityScheme
        );

        SecurityRequirement securityRequirement =
                new SecurityRequirement();

        securityRequirement.addList("bearerAuth");

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Task Management API")
                                .version("1.0")
                                .description(
                                        "Backend API for Task Management System"
                                )
                )
                .components(components)
                .addSecurityItem(
                        securityRequirement
                );
    }
}