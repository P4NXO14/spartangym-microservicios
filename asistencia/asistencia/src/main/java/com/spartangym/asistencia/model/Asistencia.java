package com.spartangym.asistencia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "asistencias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAsistencia;

    @Column(nullable = false)
    private Integer idCliente;

    @Column(nullable = false)
    private LocalDateTime fechaHoraIngreso;

    private LocalDateTime fechaHoraSalida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAsistencia estado;
}