package com.spartangym.productos.DataLoader;

import com.spartangym.productos.model.Producto;
import com.spartangym.productos.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductoDataLoader {

    @Bean
    CommandLineRunner initProductos(ProductoRepository productoRepository) {
        return args -> {

            if (productoRepository.count() == 0) {

                productoRepository.save(new Producto(
                        null,
                        "Proteina Whey",
                        25000.0,
                        20
                ));

                productoRepository.save(new Producto(
                        null,
                        "Creatina Monohidratada",
                        18000.0,
                        15
                ));

                productoRepository.save(new Producto(
                        null,
                        "Botella SpartanGYM",
                        7000.0,
                        30
                ));

                productoRepository.save(new Producto(
                        null,
                        "Guantes de Entrenamiento",
                        12000.0,
                        10
                ));
            }
        };
    }
}