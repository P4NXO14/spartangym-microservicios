package com.spartangym.logros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "logros")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLogro;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Column(nullable = false)
    private Integer idCliente;

    @NotBlank(message = "El nombre del logro es obligatorio")
    @Column(nullable = false)
    private String nombreLogro;

    @NotBlank(message = "La descripcion del logro es obligatoria")
    @Column(nullable = false)
    private String descripcion;

    private LocalDate fechaObtencion;
}