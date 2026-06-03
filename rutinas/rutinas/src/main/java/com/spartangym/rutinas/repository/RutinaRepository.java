package com.spartangym.rutinas.repository;

import com.spartangym.rutinas.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RutinaRepository extends JpaRepository<Rutina, Integer> {

    List<Rutina> findByIdCliente(Integer idCliente);

    List<Rutina> findByDificultadIgnoreCase(String dificultad);
}