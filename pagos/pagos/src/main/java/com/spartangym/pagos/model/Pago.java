package com.spartangym.pagos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPago;

    @Column(nullable = false)
    private Integer idCliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPago tipoPago;

    @Column(nullable = false)
    private Integer referenciaId;

    @Column(nullable = false)
    private Double montoCobrado;

    @Column(nullable = false)
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;
}