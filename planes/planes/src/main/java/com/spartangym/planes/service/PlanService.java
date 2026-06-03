package com.spartangym.planes.service;

import com.spartangym.planes.model.Plan;
import com.spartangym.planes.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private static final Logger logger = LoggerFactory.getLogger(PlanService.class);

    private final PlanRepository planRepository;

    public List<Plan> listarTodos() {
        logger.info("Listando todos los planes");
        return planRepository.findAll();
    }

    public Plan buscarPorId(Integer id) {
        logger.info("Buscando plan con id {}", id);

        return planRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Plan con id {} no encontrado", id);
                    return new RuntimeException("Plan no encontrado");
                });
    }

    public Plan guardar(Plan plan) {
        logger.info("Iniciando registro de plan {}", plan.getNombrePlan());

        validarPlan(plan);

        Plan planGuardado = planRepository.save(plan);

        logger.info("Plan registrado correctamente con id {}", planGuardado.getIdPlan());

        return planGuardado;
    }

    public Plan actualizar(Integer id, Plan planActualizado) {
        logger.info("Iniciando actualizacion de plan con id {}", id);

        Plan existente = buscarPorId(id);

        validarPlan(planActualizado);

        existente.setNombrePlan(planActualizado.getNombrePlan());
        existente.setDescripcion(planActualizado.getDescripcion());
        existente.setPrecio(planActualizado.getPrecio());
        existente.setDuracionDias(planActualizado.getDuracionDias());

        Plan planGuardado = planRepository.save(existente);

        logger.info("Plan actualizado correctamente con id {}", planGuardado.getIdPlan());

        return planGuardado;
    }

    public void eliminar(Integer id) {
        logger.info("Iniciando eliminacion de plan con id {}", id);

        Plan plan = buscarPorId(id);
        planRepository.delete(plan);

        logger.info("Plan eliminado correctamente con id {}", id);
    }

    private void validarPlan(Plan plan) {
        if (plan.getPrecio() == null || plan.getPrecio() <= 0) {
            logger.warn("Precio no valido para plan {}", plan.getNombrePlan());
            throw new RuntimeException("El precio debe ser mayor a 0");
        }

        if (plan.getDuracionDias() == null || plan.getDuracionDias() <= 0) {
            logger.warn("Duracion no valida para plan {}", plan.getNombrePlan());
            throw new RuntimeException("La duracion debe ser de al menos 1 dia");
        }
    }
}