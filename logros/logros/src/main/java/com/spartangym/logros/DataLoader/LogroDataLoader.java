package com.spartangym.logros.DataLoader;

import com.spartangym.logros.model.Logro;
import com.spartangym.logros.repository.LogroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class LogroDataLoader {

    @Bean
    CommandLineRunner initLogros(LogroRepository logroRepository) {
        return args -> {

            if (logroRepository.count() == 0) {

                logroRepository.save(new Logro(
                        null,
                        3,
                        "Primera asistencia",
                        "El cliente registro su primera asistencia en SpartanGYM",
                        LocalDate.now()
                ));

                logroRepository.save(new Logro(
                        null,
                        3,
                        "Primera reserva",
                        "El cliente realizo su primera reserva de clase",
                        LocalDate.now()
                ));

                logroRepository.save(new Logro(
                        null,
                        4,
                        "Cliente constante",
                        "El cliente mantiene una participacion activa en el gimnasio",
                        LocalDate.now()
                ));
            }
        };
    }
}