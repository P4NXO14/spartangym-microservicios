package com.spartangym.notificaciones.DataLoader;

import com.spartangym.notificaciones.model.Notificacion;
import com.spartangym.notificaciones.repository.NotificacionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class NotificacionDataLoader {

    @Bean
    CommandLineRunner initNotificaciones(NotificacionRepository notificacionRepository) {
        return args -> {

            if (notificacionRepository.count() == 0) {

                notificacionRepository.save(new Notificacion(
                        null,
                        3,
                        "Bienvenido a SpartanGYM",
                        "Tu cuenta ha sido registrada correctamente",
                        LocalDateTime.now()
                ));

                notificacionRepository.save(new Notificacion(
                        null,
                        3,
                        "Recordatorio de clase",
                        "Recuerda revisar tus reservas activas",
                        LocalDateTime.now()
                ));

                notificacionRepository.save(new Notificacion(
                        null,
                        4,
                        "Nuevo logro disponible",
                        "Has recibido un nuevo logro en SpartanGYM",
                        LocalDateTime.now()
                ));
            }
        };
    }
}