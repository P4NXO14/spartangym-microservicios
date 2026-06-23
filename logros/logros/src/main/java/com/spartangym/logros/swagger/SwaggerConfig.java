package com.spartangym.logros.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI logrosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Logros - SpartanGYM")
                        .description("API REST para la gestion de logros obtenidos por los clientes del gimnasio SpartanGYM")
                        .version("1.0")
                        .contact(new Contact()
                                .name("SpartanGYM")
                                .email("soporte@spartangym.com")
                        )
                );
    }
}