package com.spartangym.rutinas.DataLoader;

import com.spartangym.rutinas.model.Rutina;
import com.spartangym.rutinas.repository.RutinaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class RutinaDataLoader {

    @Bean
    CommandLineRunner initRutinas(RutinaRepository rutinaRepository) {
        return args -> {

            if (rutinaRepository.count() == 0) {

                rutinaRepository.save(new Rutina(
                        null,
                        3,
                        "Rutina fuerza inicial",
                        "Sentadillas, press banca, remo y peso muerto",
                        "Ganar fuerza",
                        "INTERMEDIA",
                        LocalDate.now()
                ));

                rutinaRepository.save(new Rutina(
                        null,
                        3,
                        "Rutina cardio",
                        "Caminadora, bicicleta y eliptica",
                        "Mejorar resistencia",
                        "BASICA",
                        LocalDate.now()
                ));

                rutinaRepository.save(new Rutina(
                        null,
                        4,
                        "Rutina hipertrofia",
                        "Trabajo dividido por grupos musculares",
                        "Aumentar masa muscular",
                        "AVANZADA",
                        LocalDate.now()
                ));
            }
        };
    }
}