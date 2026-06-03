package com.spartangym.productos.repository;

import com.spartangym.productos.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    List<Venta> findByProducto_IdProducto(Integer idProducto);
}