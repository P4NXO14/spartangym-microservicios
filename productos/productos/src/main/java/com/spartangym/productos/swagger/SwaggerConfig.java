package com.spartangym.productos.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI productosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Productos y Ventas - SpartanGYM")
                        .description("API REST para la gestion del inventario de productos y el registro de ventas del gimnasio SpartanGYM")
                        .version("1.0")
                        .contact(new Contact()
                                .name("SpartanGYM")
                                .email("soporte@spartangym.com")
                        )
                );
    }
}