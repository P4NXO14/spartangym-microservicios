package com.spartangym.pagos.repository;

import com.spartangym.pagos.model.EstadoPago;
import com.spartangym.pagos.model.Pago;
import com.spartangym.pagos.model.TipoPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByIdCliente(Integer idCliente);

    List<Pago> findByTipoPago(TipoPago tipoPago);

    List<Pago> findByIdClienteAndTipoPagoAndEstado(
            Integer idCliente,
            TipoPago tipoPago,
            EstadoPago estado
    );

    Optional<Pago> findByTipoPagoAndReferenciaId(
            TipoPago tipoPago,
            Integer referenciaId
    );

    boolean existsByTipoPagoAndReferenciaId(
            TipoPago tipoPago,
            Integer referenciaId
    );
}