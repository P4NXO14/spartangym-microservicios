package com.spartangym.planes.DataLoader;

import com.spartangym.planes.model.Plan;
import com.spartangym.planes.repository.PlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlanDataLoader {

    @Bean
    CommandLineRunner initPlanes(PlanRepository planRepository) {
        return args -> {

            if (planRepository.count() == 0) {

                planRepository.save(new Plan(
                        null,
                        "Plan Diario",
                        "Acceso al gimnasio por un dia",
                        5000.0,
                        1
                ));

                planRepository.save(new Plan(
                    null,
                    "Plan Semanal",
                    "Acceso al gimnasio por una semana",
                    12000.0,
                    7
                ));

                planRepository.save(new Plan(
                        null,
                        "Plan Mensual",
                        "Acceso al gimnasio por treinta dias",
                        30000.0,
                        30
                ));

                planRepository.save(new Plan(
                        null,
                        "Plan Trimestral",
                        "Acceso al gimnasio por noventa dias",
                        80000.0,
                        90
                ));

                planRepository.save(new Plan(
                        null,
                        "Plan Anual",
                        "Acceso al gimnasio por trescientos sesenta y cinco dias",
                        280000.0,
                        365
                ));
            }
        };
    }
}