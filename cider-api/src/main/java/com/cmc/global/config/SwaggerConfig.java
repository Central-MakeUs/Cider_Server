package com.cmc.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.url}")
    private String url;

    @Value("${swagger.desc}")
    private String desc;

    @Value("${spring.profiles.active}")
    private String profile;

    private final String IDFV_TOKEN_HEADER = "Authorization";

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI springOpenAPI() {
        SecurityScheme bearerAuth = new SecurityScheme().type(SecurityScheme.Type.APIKEY).name(IDFV_TOKEN_HEADER).in(SecurityScheme.In.HEADER);
        SecurityRequirement securityItem = new SecurityRequirement().addList(IDFV_TOKEN_HEADER);

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(IDFV_TOKEN_HEADER, bearerAuth))
                .addSecurityItem(securityItem)
                .info(new Info().title("Cider dev API")
                        .description("Cider dev API")
                        .version("v0.0.1"))
                .servers(List.of(new Server().url(url)));
    }

}
