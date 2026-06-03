package com.spartangym.reservas.repository;

import com.spartangym.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    boolean existsByClienteIdAndClase_IdClaseAndEstado(Integer clienteId, Integer idClase,String estado);
}