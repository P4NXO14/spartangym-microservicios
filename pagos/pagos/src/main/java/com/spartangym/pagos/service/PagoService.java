package com.spartangym.pagos.service;

import com.spartangym.pagos.dto.ClienteDTO;
import com.spartangym.pagos.dto.PagoPlanDTO;
import com.spartangym.pagos.dto.PagoProductoDTO;
import com.spartangym.pagos.dto.PlanDTO;
import com.spartangym.pagos.model.EstadoPago;
import com.spartangym.pagos.model.Pago;
import com.spartangym.pagos.model.TipoPago;
import com.spartangym.pagos.repository.PagoRepository;
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
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository pagoRepository;

    public List<Pago> listarTodos() {
        logger.info("Listando todos los pagos");
        return pagoRepository.findAll();
    }

    public Pago buscarPorId(Integer id) {
        logger.info("Buscando pago con id {}", id);

        return pagoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Pago con id {} no encontrado", id);
                    return new RuntimeException("Pago no encontrado");
                });
    }

    public List<Pago> listarPorCliente(Integer idCliente) {
        logger.info("Listando pagos del cliente {}", idCliente);
        return pagoRepository.findByIdCliente(idCliente);
    }

    public List<Pago> listarPorTipo(String tipoPago) {
        logger.info("Listando pagos por tipo {}", tipoPago);

        TipoPago tipo = convertirTipoPago(tipoPago);
        return pagoRepository.findByTipoPago(tipo);
    }

    public Boolean clienteTienePlanActivo(Integer idCliente) {
        logger.info("Validando si cliente {} tiene plan activo", idCliente);

        obtenerCliente(idCliente);

        boolean tienePlanActivo = existePlanActivo(idCliente);

        if (tienePlanActivo) {
            logger.info("Cliente {} tiene plan activo", idCliente);
        } else {
            logger.info("Cliente {} no tiene plan activo", idCliente);
        }

        return tienePlanActivo;
    }

    public Pago registrarPagoPlan(PagoPlanDTO pagoPlanDTO) {
        logger.info("Iniciando registro de pago de plan {} para cliente {}",
                pagoPlanDTO.getIdPlan(),
                pagoPlanDTO.getIdCliente());

        ClienteDTO cliente = obtenerCliente(pagoPlanDTO.getIdCliente());

        validarClienteParaPagoPlan(cliente, pagoPlanDTO.getIdCliente());

        PlanDTO plan = obtenerPlan(pagoPlanDTO.getIdPlan());

        validarPlan(plan, pagoPlanDTO.getIdPlan());

        if (existePlanActivo(pagoPlanDTO.getIdCliente())) {
            logger.warn("Cliente {} ya tiene un plan activo", pagoPlanDTO.getIdCliente());
            throw new RuntimeException("El cliente ya tiene un plan activo");
        }

        Pago pago = new Pago();
        pago.setIdCliente(pagoPlanDTO.getIdCliente());
        pago.setTipoPago(TipoPago.PLAN);
        pago.setReferenciaId(pagoPlanDTO.getIdPlan());
        pago.setMontoCobrado(plan.getPrecio());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado(EstadoPago.PAGADO);

        Pago pagoGuardado = pagoRepository.save(pago);

        logger.info("Pago de plan registrado correctamente con id {}", pagoGuardado.getIdPago());

        return pagoGuardado;
    }

    public Pago registrarPagoProducto(PagoProductoDTO pagoProductoDTO) {
        logger.info("Iniciando registro de pago de producto para cliente {} y venta {}",
                pagoProductoDTO.getIdCliente(),
                pagoProductoDTO.getReferenciaId());

        ClienteDTO cliente = obtenerCliente(pagoProductoDTO.getIdCliente());

        validarClienteParaPagoProducto(cliente, pagoProductoDTO.getIdCliente());

        if (pagoProductoDTO.getMontoCobrado() == null || pagoProductoDTO.getMontoCobrado() <= 0) {
            logger.warn("Monto no valido para pago de producto");
            throw new RuntimeException("El monto cobrado debe ser mayor a 0");
        }

        if (pagoProductoDTO.getReferenciaId() == null || pagoProductoDTO.getReferenciaId() <= 0) {
            logger.warn("Referencia no valida para pago de producto");
            throw new RuntimeException("La referencia de venta debe ser mayor a 0");
        }

        if (pagoRepository.existsByTipoPagoAndReferenciaId(TipoPago.PRODUCTO, pagoProductoDTO.getReferenciaId())) {
            logger.warn("La venta {} ya tiene un pago registrado", pagoProductoDTO.getReferenciaId());
            throw new RuntimeException("La venta ya tiene un pago registrado");
        }

        Pago pago = new Pago();
        pago.setIdCliente(pagoProductoDTO.getIdCliente());
        pago.setTipoPago(TipoPago.PRODUCTO);
        pago.setReferenciaId(pagoProductoDTO.getReferenciaId());
        pago.setMontoCobrado(pagoProductoDTO.getMontoCobrado());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado(EstadoPago.PAGADO);

        Pago pagoGuardado = pagoRepository.save(pago);

        logger.info("Pago de producto registrado correctamente con id {}", pagoGuardado.getIdPago());

        return pagoGuardado;
    }

    public Pago cancelarPago(Integer id) {
        logger.info("Iniciando cancelacion de pago con id {}", id);

        Pago pago = buscarPorId(id);

        if (pago.getEstado() == EstadoPago.CANCELADO) {
            logger.warn("El pago {} ya se encuentra cancelado", id);
            throw new RuntimeException("El pago ya se encuentra cancelado");
        }

        pago.setEstado(EstadoPago.CANCELADO);

        Pago pagoGuardado = pagoRepository.save(pago);

        logger.info("Pago {} cancelado correctamente", id);

        return pagoGuardado;
    }

    public Pago cancelarPorTipoYReferencia(String tipoPago, Integer referenciaId) {
        logger.info("Iniciando cancelacion de pago por tipo {} y referencia {}",
                tipoPago,
                referenciaId);

        TipoPago tipo = convertirTipoPago(tipoPago);

        Pago pago = pagoRepository.findByTipoPagoAndReferenciaId(tipo, referenciaId)
                .orElseThrow(() -> {
                    logger.warn("Pago asociado no encontrado para tipo {} y referencia {}",
                            tipoPago,
                            referenciaId);
                    return new RuntimeException("Pago asociado no encontrado");
                });

        if (pago.getEstado() == EstadoPago.CANCELADO) {
            logger.warn("El pago asociado ya se encuentra cancelado");
            throw new RuntimeException("El pago ya se encuentra cancelado");
        }

        pago.setEstado(EstadoPago.CANCELADO);

        Pago pagoGuardado = pagoRepository.save(pago);

        logger.info("Pago asociado cancelado correctamente con id {}", pagoGuardado.getIdPago());

        return pagoGuardado;
    }

    public void eliminar(Integer id) {
        logger.info("Iniciando eliminacion de pago con id {}", id);

        Pago pago = buscarPorId(id);
        pagoRepository.delete(pago);

        logger.info("Pago eliminado correctamente con id {}", id);
    }

    private boolean existePlanActivo(Integer idCliente) {
        logger.info("Consultando pagos de plan activos del cliente {}", idCliente);

        List<Pago> pagosPlanPagados = pagoRepository.findByIdClienteAndTipoPagoAndEstado(
                idCliente,
                TipoPago.PLAN,
                EstadoPago.PAGADO
        );

        LocalDateTime ahora = LocalDateTime.now();

        for (Pago pagoPlan : pagosPlanPagados) {
            PlanDTO planContratado = obtenerPlan(pagoPlan.getReferenciaId());

            validarPlan(planContratado, pagoPlan.getReferenciaId());

            LocalDateTime fechaTerminoPlan = pagoPlan.getFechaPago().plusDays(planContratado.getDuracionDias());

            if (fechaTerminoPlan.isAfter(ahora)) {
                logger.info("Cliente {} tiene plan activo hasta {}", idCliente, fechaTerminoPlan);
                return true;
            }
        }

        return false;
    }

    private void validarClienteParaPagoPlan(ClienteDTO cliente, Integer idCliente) {
        if (cliente == null) {
            logger.warn("Cliente {} no encontrado", idCliente);
            throw new RuntimeException("Cliente con id " + idCliente + " no encontrado");
        }

        if (!"Activo".equalsIgnoreCase(cliente.getEstado())) {
            logger.warn("Cliente {} no esta activo para contratar plan", idCliente);
            throw new RuntimeException("El cliente no esta activo para contratar un plan");
        }

        if (!"CLIENTE".equalsIgnoreCase(cliente.getRol())) {
            logger.warn("Cliente {} no tiene rol CLIENTE para contratar plan", idCliente);
            throw new RuntimeException("Solo los clientes pueden contratar planes");
        }
    }

    private void validarClienteParaPagoProducto(ClienteDTO cliente, Integer idCliente) {
        if (cliente == null) {
            logger.warn("Cliente {} no encontrado", idCliente);
            throw new RuntimeException("Cliente con id " + idCliente + " no encontrado");
        }

        if (!"Activo".equalsIgnoreCase(cliente.getEstado())) {
            logger.warn("Cliente {} no esta activo para pagar producto", idCliente);
            throw new RuntimeException("El cliente no esta activo para registrar pago de producto");
        }

        if (!"CLIENTE".equalsIgnoreCase(cliente.getRol())) {
            logger.warn("Cliente {} no tiene rol CLIENTE para pagar producto", idCliente);
            throw new RuntimeException("Solo los clientes pueden registrar pagos de producto");
        }
    }

    private void validarPlan(PlanDTO plan, Integer idPlan) {
        if (plan == null) {
            logger.warn("Plan {} no encontrado", idPlan);
            throw new RuntimeException("Plan con id " + idPlan + " no encontrado");
        }

        if (plan.getPrecio() == null || plan.getPrecio() <= 0) {
            logger.warn("Precio no valido para plan {}", idPlan);
            throw new RuntimeException("El precio del plan no es valido");
        }

        if (plan.getDuracionDias() == null || plan.getDuracionDias() <= 0) {
            logger.warn("Duracion no valida para plan {}", idPlan);
            throw new RuntimeException("La duracion del plan no es valida");
        }
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

        } catch (ResourceAccessException e) {
            logger.error("El microservicio de clientes no se encuentra disponible");
            throw new RuntimeException("El microservicio de clientes no se encuentra disponible");

        } catch (Exception e) {
            logger.error("Error al consultar el cliente {}", idCliente);
            throw new RuntimeException("Error al consultar el cliente");
        }
    }

    private PlanDTO obtenerPlan(Integer idPlan) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8087/api/planes/" + idPlan;

        try {
            logger.info("Consultando plan {} desde ms-planes", idPlan);
            return restTemplate.getForObject(url, PlanDTO.class);

        } catch (HttpStatusCodeException e) {
            logger.warn("Error al consultar plan {} desde ms-planes", idPlan);
            throw new RuntimeException("Plan con id " + idPlan + " no encontrado");

        } catch (ResourceAccessException e) {
            logger.error("El microservicio de planes no se encuentra disponible");
            throw new RuntimeException("El microservicio de planes no se encuentra disponible");

        } catch (Exception e) {
            logger.error("Error al consultar el plan {}", idPlan);
            throw new RuntimeException("Error al consultar el plan");
        }
    }

    private TipoPago convertirTipoPago(String tipoPago) {
        for (TipoPago tipo : TipoPago.values()) {
            if (tipo.name().equalsIgnoreCase(tipoPago)) {
                return tipo;
            }
        }

        logger.warn("Tipo de pago no valido: {}", tipoPago);
        throw new RuntimeException("Tipo de pago no valido. Use PLAN o PRODUCTO");
    }
}