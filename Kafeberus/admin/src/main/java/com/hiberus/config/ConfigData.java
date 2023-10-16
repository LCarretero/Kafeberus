package com.hiberus.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ConfigData {
    private static final String TITLE = "Pizzas-read API";
    private static final String DESCRIPTION = "API Pizzas for reading from the BD";
    private static final String BASE_PACKAGE = "com.hiberus.controllers";
    private static final String VERSION = "v1";

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder().group("api-web").packagesToScan(BASE_PACKAGE)
                .build();
    }

    /**
     * Creates Springdoc object where it is defined or described the API definition.
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info().title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION));

    }

}
