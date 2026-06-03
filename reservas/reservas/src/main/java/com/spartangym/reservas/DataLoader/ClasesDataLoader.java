package com.spartangym.reservas.DataLoader;

import com.spartangym.reservas.model.Clase;
import com.spartangym.reservas.repository.ClaseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class ClasesDataLoader {

    @Bean
    CommandLineRunner initClases(ClaseRepository claseRepository){
        return args ->{

            if(claseRepository.count()==0){

                claseRepository.save(new Clase(null,"Aerobicos",10,10,LocalDate.now().plusDays(1),LocalTime.of(17, 30)));
                claseRepository.save(new Clase(null,"Crossfit",10,5,LocalDate.now().plusDays(1),LocalTime.of(19, 30)));
                claseRepository.save(new Clase(null,"Pesas",20,8,LocalDate.now().plusDays(1),LocalTime.of(18, 30)));
                claseRepository.save(new Clase(null,"Cardio",15,9,LocalDate.now().plusDays(1),LocalTime.of(16, 30)));
            }
        };
    }

}
