package com.spartangym.asistencia.service;

import com.spartangym.asistencia.dto.ClienteDTO;
import com.spartangym.asistencia.model.Asistencia;
import com.spartangym.asistencia.model.EstadoAsistencia;
import com.spartangym.asistencia.repository.AsistenciaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private static final Logger logger = LoggerFactory.getLogger(AsistenciaService.class);

    private final AsistenciaRepository asistenciaRepository;

    public List<Asistencia> listarTodas() {
        logger.info("Listando todas las asistencias");
        return asistenciaRepository.findAll();
    }

    public List<Asistencia> listarPorCliente(Integer idCliente) {
        logger.info("Listando asistencias del cliente {}", idCliente);
        return asistenciaRepository.findByIdCliente(idCliente);
    }

    public List<Asistencia> listarPorEstado(String estado) {
        logger.info("Listando asistencias por estado {}", estado);

        EstadoAsistencia estadoAsistencia = convertirEstado(estado);
        return asistenciaRepository.findByEstado(estadoAsistencia);
    }

    public Asistencia buscarPorId(Integer id) {
        logger.info("Buscando asistencia con id {}", id);

        return asistenciaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Registro de asistencia con id {} no encontrado", id);
                    return new RuntimeException("Registro de asistencia con ID " + id + " no encontrado.");
                });
    }

    public Asistencia registrarIngreso(Asistencia asistencia) {
        logger.info("Iniciando registro de ingreso para cliente {}", asistencia.getIdCliente());

        if (asistencia.getIdCliente() == null) {
            logger.warn("Intento de registrar asistencia sin idCliente");
            throw new RuntimeException("El ID del cliente es obligatorio");
        }

        ClienteDTO cliente = obtenerCliente(asistencia.getIdCliente());

        if (cliente == null) {
            logger.warn("Cliente {} no existe", asistencia.getIdCliente());
            throw new RuntimeException("Cliente con id " + asistencia.getIdCliente() + " no encontrado");
        }

        if (!"Activo".equalsIgnoreCase(cliente.getEstado())) {
            logger.warn("Cliente {} no esta activo para registrar asistencia", asistencia.getIdCliente());
            throw new RuntimeException("El cliente no esta activo para registrar asistencia");
        }

        if (!"CLIENTE".equalsIgnoreCase(cliente.getRol())) {
            logger.warn("Cliente {} no tiene rol CLIENTE para registrar asistencia", asistencia.getIdCliente());
            throw new RuntimeException("Solo los clientes pueden registrar asistencia");
        }

        if (asistenciaRepository.existsByIdClienteAndEstado(asistencia.getIdCliente(), EstadoAsistencia.REGISTRADA)) {
            logger.warn("Cliente {} ya tiene una asistencia registrada sin salida", asistencia.getIdCliente());
            throw new RuntimeException("El cliente ya tiene una asistencia registrada sin salida");
        }

        asistencia.setFechaHoraIngreso(LocalDateTime.now());
        asistencia.setFechaHoraSalida(null);
        asistencia.setEstado(EstadoAsistencia.REGISTRADA);

        Asistencia asistenciaGuardada = asistenciaRepository.save(asistencia);

        logger.info("Asistencia registrada correctamente con id {} para cliente {}",
                asistenciaGuardada.getIdAsistencia(),
                asistenciaGuardada.getIdCliente());

        return asistenciaGuardada;
    }

    public Asistencia registrarSalida(Integer id) {
        logger.info("Iniciando registro de salida para asistencia {}", id);

        Asistencia asistencia = buscarPorId(id);

        if (asistencia.getEstado() == EstadoAsistencia.CANCELADA) {
            logger.warn("No se puede registrar salida de asistencia cancelada {}", id);
            throw new RuntimeException("No se puede registrar salida de una asistencia cancelada");
        }

        if (asistencia.getEstado() == EstadoAsistencia.COMPLETADA) {
            logger.warn("La asistencia {} ya fue completada anteriormente", id);
            throw new RuntimeException("La asistencia ya fue completada anteriormente");
        }

        asistencia.setFechaHoraSalida(LocalDateTime.now());
        asistencia.setEstado(EstadoAsistencia.COMPLETADA);

        Asistencia asistenciaGuardada = asistenciaRepository.save(asistencia);

        logger.info("Salida registrada correctamente para asistencia {}", id);

        return asistenciaGuardada;
    }

    public Asistencia cancelarAsistencia(Integer id) {
        logger.info("Iniciando cancelacion de asistencia {}", id);

        Asistencia asistencia = buscarPorId(id);

        if (asistencia.getEstado() == EstadoAsistencia.COMPLETADA) {
            logger.warn("No se puede cancelar asistencia completada {}", id);
            throw new RuntimeException("No se puede cancelar una asistencia ya completada");
        }

        if (asistencia.getEstado() == EstadoAsistencia.CANCELADA) {
            logger.warn("La asistencia {} ya se encuentra cancelada", id);
            throw new RuntimeException("La asistencia ya se encuentra cancelada");
        }

        asistencia.setFechaHoraSalida(LocalDateTime.now());
        asistencia.setEstado(EstadoAsistencia.CANCELADA);

        Asistencia asistenciaGuardada = asistenciaRepository.save(asistencia);

        logger.info("Asistencia {} cancelada correctamente", id);

        return asistenciaGuardada;
    }

    public void eliminar(Integer id) {
        logger.info("Iniciando eliminacion de asistencia {}", id);

        buscarPorId(id);
        asistenciaRepository.deleteById(id);

        logger.info("Asistencia eliminada correctamente con id {}", id);
    }

    private ClienteDTO obtenerCliente(Integer idCliente) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/clientes/" + idCliente;

        try {
            logger.info("Consultando cliente {} desde ms-clientes", idCliente);
            return restTemplate.getForObject(url, ClienteDTO.class);

        } catch (HttpStatusCodeException e) {
            logger.warn("Cliente {} no encontrado en ms-clientes", idCliente);
            throw new RuntimeException("Cliente con id " + idCliente + " no encontrado");

        } catch (Exception e) {
            logger.error("El microservicio de clientes no se encuentra disponible para cliente {}", idCliente);
            throw new RuntimeException("El microservicio de clientes no se encuentra disponible");
        }
    }

    private EstadoAsistencia convertirEstado(String estado) {
        logger.info("Convirtiendo estado de asistencia {}", estado);

        for (EstadoAsistencia estadoAsistencia : EstadoAsistencia.values()) {
            if (estadoAsistencia.name().equalsIgnoreCase(estado)) {
                return estadoAsistencia;
            }
        }

        logger.warn("Estado de asistencia no valido: {}", estado);
        throw new RuntimeException("Estado de asistencia no valido. Use REGISTRADA, COMPLETADA o CANCELADA");
    }
}