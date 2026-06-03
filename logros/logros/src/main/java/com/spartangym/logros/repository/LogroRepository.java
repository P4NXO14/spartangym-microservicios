package com.spartangym.logros.repository;

import com.spartangym.logros.model.Logro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogroRepository extends JpaRepository<Logro, Integer> {

    List<Logro> findByIdCliente(Integer idCliente);

    boolean existsByIdClienteAndNombreLogroIgnoreCase(Integer idCliente, String nombreLogro);
}