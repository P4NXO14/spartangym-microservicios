package com.spartangym.logros.service;

import com.spartangym.logros.dto.ClienteDTO;
import com.spartangym.logros.model.Logro;
import com.spartangym.logros.repository.LogroRepository;
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
public class LogroService {

    private static final Logger logger = LoggerFactory.getLogger(LogroService.class);

    private final LogroRepository logroRepository;

    public List<Logro> listarTodos() {
        logger.info("Listando todos los logros");
        return logroRepository.findAll();
    }

    public Logro buscarPorId(Integer id) {
        logger.info("Buscando logro con id {}", id);

        return logroRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Logro con id {} no encontrado", id);
                    return new RuntimeException("Logro no encontrado");
                });
    }

    public List<Logro> listarPorCliente(Integer idCliente) {
        logger.info("Listando logros del cliente {}", idCliente);

        validarCliente(idCliente);

        return logroRepository.findByIdCliente(idCliente);
    }

    public Logro guardar(Logro logro) {
        logger.info("Iniciando registro de logro para cliente {}", logro.getIdCliente());

        ClienteDTO cliente = validarCliente(logro.getIdCliente());

        if (logroRepository.existsByIdClienteAndNombreLogroIgnoreCase(
                logro.getIdCliente(),
                logro.getNombreLogro())) {

            logger.warn("Cliente {} ya tiene registrado el logro {}",
                    logro.getIdCliente(),
                    logro.getNombreLogro());

            throw new RuntimeException("El cliente ya tiene este logro registrado");
        }

        logro.setIdCliente(cliente.getIdCliente());
        logro.setFechaObtencion(LocalDate.now());

        Logro logroGuardado = logroRepository.save(logro);

        logger.info("Logro registrado correctamente con id {} para cliente {}",
                logroGuardado.getIdLogro(),
                logroGuardado.getIdCliente());

        return logroGuardado;
    }

    public void eliminar(Integer id) {
        logger.info("Iniciando eliminacion de logro con id {}", id);

        Logro logro = buscarPorId(id);
        logroRepository.delete(logro);

        logger.info("Logro eliminado correctamente con id {}", id);
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
            logger.warn("Cliente {} no esta activo para registrar logros", idCliente);
            throw new RuntimeException("El cliente no esta activo para registrar logros");
        }

        if (!"CLIENTE".equalsIgnoreCase(cliente.getRol())) {
            logger.warn("Cliente {} no tiene rol CLIENTE para recibir logros", idCliente);
            throw new RuntimeException("Solo los clientes pueden recibir logros");
        }

        return cliente;
    }
}