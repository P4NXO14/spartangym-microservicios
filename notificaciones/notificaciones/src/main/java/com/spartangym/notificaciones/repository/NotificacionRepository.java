package com.spartangym.notificaciones.repository;

import com.spartangym.notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByIdCliente(Integer idCliente);
}