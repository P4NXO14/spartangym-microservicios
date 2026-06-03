package com.spartangym.reservas.repository;

import com.spartangym.reservas.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Integer> {

    List<Clase> findByNombreClaseIgnoreCaseAndFecha(String nombreClase, LocalDate fecha);
}