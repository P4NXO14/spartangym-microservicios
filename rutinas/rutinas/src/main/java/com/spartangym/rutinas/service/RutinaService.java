package com.spartangym.rutinas.service;

import com.spartangym.rutinas.dto.ClienteDTO;
import com.spartangym.rutinas.model.Rutina;
import com.spartangym.rutinas.repository.RutinaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RutinaService {

    private static final Logger logger = LoggerFactory.getLogger(RutinaService.class);

    private final RutinaRepository rutinaRepository;

    public List<Rutina> listarTodas() {
        logger.info("Listando todas las rutinas");
        return rutinaRepository.findAll();
    }

    public Rutina buscarPorId(Integer id) {
        logger.info("Buscando rutina con id {}", id);

        return rutinaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Rutina con id {} no encontrada", id);
                    return new RuntimeException("Rutina no encontrada");
                });
    }

    public List<Rutina> listarPorCliente(Integer idCliente) {
        logger.info("Listando rutinas del cliente {}", idCliente);

        validarCliente(idCliente);

        return rutinaRepository.findByIdCliente(idCliente);
    }

    public List<Rutina> listarPorDificultad(String dificultad) {
        logger.info("Listando rutinas por dificultad {}", dificultad);
        return rutinaRepository.findByDificultadIgnoreCase(dificultad);
    }

    public Rutina guardar(Rutina rutina) {
        logger.info("Iniciando registro de rutina para cliente {}", rutina.getIdCliente());

        ClienteDTO cliente = validarCliente(rutina.getIdCliente());

        rutina.setIdCliente(cliente.getIdCliente());
        rutina.setFechaAsignacion(LocalDate.now());

        Rutina rutinaGuardada = rutinaRepository.save(rutina);

        logger.info("Rutina registrada correctamente con id {} para cliente {}",
                rutinaGuardada.getIdRutina(),
                rutinaGuardada.getIdCliente());

        return rutinaGuardada;
    }

    public Rutina actualizar(Integer id, Rutina rutinaActualizada) {
        logger.info("Iniciando actualizacion de rutina con id {}", id);

        Rutina existente = buscarPorId(id);

        ClienteDTO cliente = validarCliente(rutinaActualizada.getIdCliente());

        existente.setIdCliente(cliente.getIdCliente());
        existente.setNombreRutina(rutinaActualizada.getNombreRutina());
        existente.setDescripcion(rutinaActualizada.getDescripcion());
        existente.setObjetivo(rutinaActualizada.getObjetivo());
        existente.setDificultad(rutinaActualizada.getDificultad());

        Rutina rutinaGuardada = rutinaRepository.save(existente);

        logger.info("Rutina actualizada correctamente con id {}", rutinaGuardada.getIdRutina());

        return rutinaGuardada;
    }

    public void eliminar(Integer id) {
        logger.info("Iniciando eliminacion de rutina con id {}", id);

        Rutina rutina = buscarPorId(id);
        rutinaRepository.delete(rutina);

        logger.info("Rutina eliminada correctamente con id {}", id);
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
            logger.warn("Cliente {} no esta activo para asignar rutinas", idCliente);
            throw new RuntimeException("El cliente no esta activo para asignar rutinas");
        }

        if (!"CLIENTE".equalsIgnoreCase(cliente.getRol())) {
            logger.warn("Cliente {} no tiene rol CLIENTE para recibir rutinas", idCliente);
            throw new RuntimeException("Solo los clientes pueden recibir rutinas");
        }

        return cliente;
    }
}