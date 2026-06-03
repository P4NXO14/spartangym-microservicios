package com.spartangym.productos.service;

import com.spartangym.productos.dto.PagoProductoDTO;
import com.spartangym.productos.dto.VentaDTO;
import com.spartangym.productos.model.EstadoVenta;
import com.spartangym.productos.model.Producto;
import com.spartangym.productos.model.Venta;
import com.spartangym.productos.repository.ProductoRepository;
import com.spartangym.productos.repository.VentaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private static final Logger logger = LoggerFactory.getLogger(VentaService.class);

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public List<Venta> listarTodas() {
        logger.info("Listando todas las ventas");
        return ventaRepository.findAll();
    }

    public Venta buscarPorId(Integer id) {
        logger.info("Buscando venta con id {}", id);

        return ventaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Venta con id {} no encontrada", id);
                    return new RuntimeException("Venta no encontrada");
                });
    }

    public List<Venta> listarPorProducto(Integer idProducto) {
        logger.info("Listando ventas del producto {}", idProducto);

        productoRepository.findById(idProducto)
                .orElseThrow(() -> {
                    logger.warn("Producto con id {} no encontrado", idProducto);
                    return new RuntimeException("Producto no encontrado");
                });

        return ventaRepository.findByProducto_IdProducto(idProducto);
    }

    @Transactional
    public Venta vender(Integer idProducto, VentaDTO ventaDTO) {
        logger.info("Iniciando venta de producto {} para cliente {}",
                idProducto,
                ventaDTO.getIdCliente());

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> {
                    logger.warn("Producto con id {} no encontrado para venta", idProducto);
                    return new RuntimeException("Producto no encontrado");
                });

        if (ventaDTO.getCantidad() > producto.getStock()) {
            logger.warn("Stock insuficiente para producto {}. Stock actual: {}, cantidad solicitada: {}",
                    idProducto,
                    producto.getStock(),
                    ventaDTO.getCantidad());

            throw new RuntimeException("Stock insuficiente para realizar la venta");
        }

        producto.setStock(producto.getStock() - ventaDTO.getCantidad());
        productoRepository.save(producto);

        Venta venta = new Venta();
        venta.setProducto(producto);
        venta.setIdCliente(ventaDTO.getIdCliente());
        venta.setCantidad(ventaDTO.getCantidad());
        venta.setTotal(producto.getPrecio() * ventaDTO.getCantidad());
        venta.setFechaVenta(LocalDateTime.now());
        venta.setEstado(EstadoVenta.REALIZADA);

        Venta ventaGuardada = ventaRepository.save(venta);

        logger.info("Venta registrada correctamente con id {} para cliente {}",
                ventaGuardada.getIdVenta(),
                ventaGuardada.getIdCliente());

        registrarPagoProducto(ventaGuardada);

        return ventaGuardada;
    }

    @Transactional
    public Venta cancelarVenta(Integer id) {
        logger.info("Iniciando cancelacion de venta con id {}", id);

        Venta venta = buscarPorId(id);

        if (venta.getEstado() == EstadoVenta.CANCELADA) {
            logger.warn("La venta {} ya se encuentra cancelada", id);
            throw new RuntimeException("La venta ya se encuentra cancelada");
        }

        Producto producto = venta.getProducto();

        producto.setStock(producto.getStock() + venta.getCantidad());
        productoRepository.save(producto);

        venta.setEstado(EstadoVenta.CANCELADA);

        Venta ventaGuardada = ventaRepository.save(venta);

        logger.info("Venta {} cancelada correctamente. Stock devuelto al producto {}. Stock actual: {}",
                ventaGuardada.getIdVenta(),
                producto.getIdProducto(),
                producto.getStock());

        return ventaGuardada;
    }

    public void eliminar(Integer id) {
        logger.info("Iniciando eliminacion de venta con id {}", id);

        Venta venta = buscarPorId(id);
        ventaRepository.delete(venta);

        logger.info("Venta eliminada correctamente con id {}", id);
    }

    private void registrarPagoProducto(Venta venta) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8086/api/pagos/producto";

        PagoProductoDTO pagoProductoDTO = new PagoProductoDTO(
                venta.getIdCliente(),
                venta.getIdVenta(),
                venta.getTotal()
        );

        logger.info("Enviando pago de producto a ms-pagos para venta {}", venta.getIdVenta());

        try {
            restTemplate.postForObject(url, pagoProductoDTO, Object.class);

            logger.info("Pago de producto enviado correctamente a ms-pagos para venta {}",
                    venta.getIdVenta());

        } catch (ResourceAccessException e) {
            logger.error("El microservicio de pagos no se encuentra disponible para venta {}",
                    venta.getIdVenta());

            throw new RuntimeException("El microservicio de pagos no se encuentra disponible");

        } catch (Exception e) {
            logger.error("Error al registrar pago del producto para venta {}",
                    venta.getIdVenta());

            throw new RuntimeException("Error al registrar el pago del producto");
        }
    }
}