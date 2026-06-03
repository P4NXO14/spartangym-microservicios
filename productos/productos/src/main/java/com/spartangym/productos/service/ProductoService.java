package com.spartangym.productos.service;

import com.spartangym.productos.model.Producto;
import com.spartangym.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        logger.info("Listando todos los productos");
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Integer id) {
        logger.info("Buscando producto con id {}", id);

        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Producto con id {} no encontrado", id);
                    return new RuntimeException("Producto no encontrado");
                });
    }

    public Producto guardar(Producto producto) {
        logger.info("Iniciando registro de producto {}", producto.getNombre());

        validarProducto(producto);

        Producto productoGuardado = productoRepository.save(producto);

        logger.info("Producto registrado correctamente con id {}", productoGuardado.getIdProducto());

        return productoGuardado;
    }

    public Producto actualizar(Integer id, Producto productoActualizado) {
        logger.info("Iniciando actualizacion de producto con id {}", id);

        Producto existente = buscarPorId(id);

        validarProducto(productoActualizado);

        existente.setNombre(productoActualizado.getNombre());
        existente.setPrecio(productoActualizado.getPrecio());
        existente.setStock(productoActualizado.getStock());

        Producto productoGuardado = productoRepository.save(existente);

        logger.info("Producto actualizado correctamente con id {}", productoGuardado.getIdProducto());

        return productoGuardado;
    }

    public void eliminar(Integer id) {
        logger.info("Iniciando eliminacion de producto con id {}", id);

        Producto producto = buscarPorId(id);
        productoRepository.delete(producto);

        logger.info("Producto eliminado correctamente con id {}", id);
    }

    private void validarProducto(Producto producto) {
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            logger.warn("Precio no valido para producto {}", producto.getNombre());
            throw new RuntimeException("El precio debe ser mayor a 0");
        }

        if (producto.getStock() == null || producto.getStock() < 0) {
            logger.warn("Stock no valido para producto {}", producto.getNombre());
            throw new RuntimeException("El stock no puede ser negativo");
        }
    }
}