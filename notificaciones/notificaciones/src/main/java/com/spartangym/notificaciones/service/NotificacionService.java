package com.spartangym.notificaciones.service;

import com.spartangym.notificaciones.dto.ClienteDTO;
import com.spartangym.notificaciones.model.Notificacion;
import com.spartangym.notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository notificacionRepository;

    public List<Notificacion> listarTodas() {
        logger.info("Listando todas las notificaciones");
        return notificacionRepository.findAll();
    }

    public Notificacion buscarPorId(Integer id) {
        logger.info("Buscando notificacion con id {}", id);

        return notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Notificacion con id {} no encontrada", id);
                    return new RuntimeException("Notificacion no encontrada");
                });
    }

    public List<Notificacion> listarPorCliente(Integer idCliente) {
        logger.info("Listando notificaciones del cliente {}", idCliente);

        validarCliente(idCliente);

        return notificacionRepository.findByIdCliente(idCliente);
    }

    public Notificacion guardar(Notificacion notificacion) {
        logger.info("Iniciando registro de notificacion para cliente {}", notificacion.getIdCliente());

        ClienteDTO cliente = validarCliente(notificacion.getIdCliente());

        notificacion.setIdCliente(cliente.getIdCliente());
        notificacion.setFechaEnvio(LocalDateTime.now());

        Notificacion notificacionGuardada = notificacionRepository.save(notificacion);

        logger.info("Notificacion registrada correctamente con id {} para cliente {}",
                notificacionGuardada.getIdNotificacion(),
                notificacionGuardada.getIdCliente());

        return notificacionGuardada;
    }

    public void eliminar(Integer id) {
        logger.info("Iniciando eliminacion de notificacion con id {}", id);

        Notificacion notificacion = buscarPorId(id);
        notificacionRepository.delete(notificacion);

        logger.info("Notificacion eliminada correctamente con id {}", id);
    }

    private ClienteDTO validarCliente(Integer idCliente) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/clientes/" + idCliente;

        logger.info("Consultando cliente {} en ms-clientes", idCliente);

        ClienteDTO cliente;

        try {
            cliente = restTemplate.getForObject(url, ClienteDTO.class);
            logger.info("Cliente {} consultado correctamente desde ms-clientes", idCliente);
        } catch (ResourceAccessException e) {
            logger.error("El microservicio de clientes no se encuentra disponible para cliente {}", idCliente);
            throw new RuntimeException("El microservicio de clientes no se encuentra disponible");
        } catch (HttpStatusCodeException e) {
            logger.warn("Cliente {} no encontrado en ms-clientes", idCliente);
            throw new RuntimeException("Cliente no encontrado");
        } catch (Exception e) {
            logger.error("Error al consultar el microservicio de clientes para cliente {}", idCliente);
            throw new RuntimeException("Error al consultar el microservicio de clientes");
        }

        if (cliente == null) {
            logger.warn("Cliente {} no encontrado", idCliente);
            throw new RuntimeException("Cliente no encontrado");
        }

        if (!"Activo".equalsIgnoreCase(cliente.getEstado())) {
            logger.warn("Cliente {} no esta activo para recibir notificaciones", idCliente);
            throw new RuntimeException("El cliente no esta activo para recibir notificaciones");
        }

        if (!"CLIENTE".equalsIgnoreCase(cliente.getRol())) {
            logger.warn("Cliente {} no tiene rol CLIENTE para recibir notificaciones", idCliente);
            throw new RuntimeException("Solo los clientes pueden recibir notificaciones");
        }

        return cliente;
    }
}