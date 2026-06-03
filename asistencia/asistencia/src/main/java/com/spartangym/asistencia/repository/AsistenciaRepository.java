package com.spartangym.asistencia.repository;

import com.spartangym.asistencia.model.Asistencia;
import com.spartangym.asistencia.model.EstadoAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {

    List<Asistencia> findByIdCliente(Integer idCliente);
    List<Asistencia> findByEstado(EstadoAsistencia estado);
    boolean existsByIdClienteAndEstado(Integer idCliente, EstadoAsistencia estado);

}