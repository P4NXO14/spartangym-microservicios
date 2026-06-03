package com.spartangym.reservas.service;

import com.spartangym.reservas.model.Clase;
import com.spartangym.reservas.repository.ClaseRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaseService {

    private static final Logger logger = LoggerFactory.getLogger(ClaseService.class);

    private final ClaseRepository claseRepository;

    public List<Clase> listarTodas() {
        logger.info("Listando todas las clases");
        return claseRepository.findAll();
    }

    public Clase buscarPorId(Integer id) {
        logger.info("Buscando clase con id {}", id);

        return claseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Clase con id {} no encontrada", id);
                    return new RuntimeException("Clase con ID " + id + " no encontrada.");
                });
    }

    public Clase guardar(Clase clase) {
        logger.info("Iniciando registro de clase {}", clase.getNombreClase());

        validarCupos(clase);
        validarHorarioClase(clase, null);

        Clase claseGuardada = claseRepository.save(clase);

        logger.info("Clase registrada correctamente con id {}", claseGuardada.getIdClase());

        return claseGuardada;
    }

    public Clase actualizar(Integer id, Clase claseActualizada) {
        logger.info("Iniciando actualizacion de clase con id {}", id);

        Clase existente = claseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Clase con id {} no encontrada", id);
                    return new RuntimeException("Clase no encontrada");
                });

        validarCupos(claseActualizada);
        validarHorarioClase(claseActualizada, id);

        existente.setNombreClase(claseActualizada.getNombreClase());
        existente.setCuposTotales(claseActualizada.getCuposTotales());
        existente.setCuposDisponibles(claseActualizada.getCuposDisponibles());
        existente.setFecha(claseActualizada.getFecha());
        existente.setHora(claseActualizada.getHora());

        Clase claseGuardada = claseRepository.save(existente);

        logger.info("Clase actualizada correctamente con id {}", claseGuardada.getIdClase());

        return claseGuardada;
    }

    @Transactional
    public Clase reservar(Integer id) {
        logger.info("Intentando reservar cupo para clase {}", id);

        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Clase con id {} no existe", id);
                    return new RuntimeException("La clase no existe.");
                });

        if (clase.getCuposDisponibles() <= 0) {
            logger.warn("No hay cupos disponibles para clase {}", clase.getIdClase());
            throw new RuntimeException("No hay cupos disponibles para la clase: " + clase.getNombreClase());
        }

        clase.setCuposDisponibles(clase.getCuposDisponibles() - 1);

        Clase claseGuardada = claseRepository.save(clase);

        logger.info("Cupo reservado correctamente para clase {}. Cupos disponibles: {}",
                claseGuardada.getIdClase(),
                claseGuardada.getCuposDisponibles());

        return claseGuardada;
    }

    public void eliminar(Integer id) {
        logger.info("Iniciando eliminacion de clase con id {}", id);

        buscarPorId(id);
        claseRepository.deleteById(id);

        logger.info("Clase eliminada correctamente con id {}", id);
    }

    private void validarCupos(Clase clase) {
        if (clase.getCuposDisponibles() > clase.getCuposTotales()) {
            logger.warn("Cupos disponibles mayores que cupos totales en clase {}", clase.getNombreClase());
            throw new RuntimeException("Los cupos disponibles no pueden ser mayores que los cupos totales.");
        }
    }

    private void validarHorarioClase(Clase clase, Integer idClaseActual) {
        logger.info("Validando horario para clase {} el dia {}", clase.getNombreClase(), clase.getFecha());

        if (clase.getNombreClase() == null || clase.getNombreClase().trim().isEmpty()) {
            logger.warn("Intento de registrar clase sin nombre");
            throw new RuntimeException("El nombre de la clase es obligatorio.");
        }

        if (clase.getFecha() == null) {
            logger.warn("Intento de registrar clase sin fecha");
            throw new RuntimeException("La fecha de la clase es obligatoria.");
        }

        if (clase.getHora() == null) {
            logger.warn("Intento de registrar clase sin hora");
            throw new RuntimeException("La hora de la clase es obligatoria.");
        }

        List<Clase> clasesDelMismoTipo = claseRepository.findByNombreClaseIgnoreCaseAndFecha(
                clase.getNombreClase(),
                clase.getFecha()
        );

        for (Clase claseExistente : clasesDelMismoTipo) {

            if (idClaseActual != null && claseExistente.getIdClase().equals(idClaseActual)) {
                continue;
            }

            long diferenciaMinutos = Math.abs(Duration.between(
                    claseExistente.getHora(),
                    clase.getHora()
            ).toMinutes());

            if (diferenciaMinutos < 90) {
                logger.warn("Conflicto de horario para clase {}. Hora existente: {}, hora solicitada: {}",
                        clase.getNombreClase(),
                        claseExistente.getHora(),
                        clase.getHora());

                throw new RuntimeException("Ya existe una clase del mismo tipo en ese horario. Debe existir una diferencia minima de 01:30:00 entre clases del mismo tipo el mismo dia.");
            }
        }
    }
}