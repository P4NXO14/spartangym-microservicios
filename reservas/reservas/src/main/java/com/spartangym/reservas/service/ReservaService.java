package com.spartangym.reservas.service;

import com.spartangym.reservas.dto.ClienteDTO;
import com.spartangym.reservas.model.Clase;
import com.spartangym.reservas.model.Reserva;
import com.spartangym.reservas.repository.ClaseRepository;
import com.spartangym.reservas.repository.ReservaRepository;

import jakarta.transaction.Transactional;
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
@Transactional
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;

    private final ClaseService claseService;

    private final ClaseRepository claseRepository;

    public Reserva generarReserva(Integer clienteId, Integer claseId) {
        logger.info("Iniciando reserva para cliente {} y clase {}", clienteId, claseId);

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/clientes/" + clienteId;

        ClienteDTO cliente;
        try {
            cliente = restTemplate.getForObject(url, ClienteDTO.class);
            logger.info("Cliente {} consultado correctamente desde ms-clientes", clienteId);
        } catch (HttpStatusCodeException e) {
            logger.warn("Error al consultar cliente {} en ms-clientes", clienteId);
            throw new RuntimeException("Cliente con id " + clienteId + " no encontrado");
        } catch (Exception e) {
            logger.error("El microservicio de clientes no se encuentra disponible para cliente {}", clienteId);
            throw new RuntimeException("El microservicio de clientes no se encuentra disponible");
        }

        if (cliente == null || !"Activo".equalsIgnoreCase(cliente.getEstado())) {
            logger.warn("Cliente {} no esta activo para poder realizar reservas", clienteId);
            throw new RuntimeException("El cliente no esta activo para poder realizar reservas");
        }

        if (!"CLIENTE".equalsIgnoreCase(cliente.getRol())) {
            logger.warn("Cliente {} no tiene rol CLIENTE para realizar reservas", clienteId);
            throw new RuntimeException("Solo los clientes pueden realizar reservas");
        }

        if (reservaRepository.existsByClienteIdAndClase_IdClaseAndEstado(clienteId, claseId, "CONFIRMADA")) {
            logger.warn("Cliente {} ya tiene una reserva confirmada para clase {}", clienteId, claseId);
            throw new RuntimeException("El cliente ya tiene una reserva para esta clase.");
        }

        Clase clase = claseService.reservar(claseId);

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setClase(clase);
        nuevaReserva.setClienteId(clienteId);
        nuevaReserva.setFechaReserva(LocalDateTime.now());
        nuevaReserva.setEstado("CONFIRMADA");

        Reserva reservaGuardada = reservaRepository.save(nuevaReserva);

        logger.info("Reserva creada correctamente con id {} para cliente {}",
                reservaGuardada.getIdReserva(),
                clienteId);

        return reservaGuardada;
    }

    public Reserva cancelarReserva(Integer idReserva) {
        logger.info("Iniciando cancelacion de reserva {}", idReserva);

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> {
                    logger.warn("Reserva {} no encontrada", idReserva);
                    return new RuntimeException("Reserva no encontrada");
                });

        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            logger.warn("Reserva {} ya esta cancelada", idReserva);
            throw new RuntimeException("La reserva ya esta cancelada.");
        }

        reserva.setEstado("CANCELADA");

        Clase clase = reserva.getClase();
        clase.setCuposDisponibles(clase.getCuposDisponibles() + 1);

        claseRepository.save(clase);

        Reserva reservaGuardada = reservaRepository.save(reserva);

        logger.info("Reserva {} cancelada correctamente", idReserva);

        return reservaGuardada;
    }

    public List<Reserva> listaReservas() {
        logger.info("Listando todas las reservas");
        return reservaRepository.findAll();
    }

    public void eliminarReserva(Integer idReserva) {
        logger.info("Iniciando eliminacion de reserva {}", idReserva);

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> {
                    logger.warn("Reserva {} no encontrada", idReserva);
                    return new RuntimeException("Reserva no encontrada");
                });

        if ("CONFIRMADA".equalsIgnoreCase(reserva.getEstado())) {
            Clase clase = reserva.getClase();
            clase.setCuposDisponibles(clase.getCuposDisponibles() + 1);
            claseRepository.save(clase);

            logger.info("Cupo devuelto a clase {} por eliminacion de reserva {}",
                    clase.getIdClase(),
                    idReserva);
        }

        reservaRepository.delete(reserva);

        logger.info("Reserva eliminada correctamente con id {}", idReserva);
    }
}