package com.spartangym.reservas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idReserva;

    @ManyToOne
    @JoinColumn(name = "id_clase", nullable = false)
    private Clase clase;

    @Column(name = "cliente_id",nullable = false)
    private Integer clienteId;

    @Column(name = "fecha_reserva")
    private LocalDateTime fechaReserva;

    @Column(nullable = false)
    private String estado;
    
}