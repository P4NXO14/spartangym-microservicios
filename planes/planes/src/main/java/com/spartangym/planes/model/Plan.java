package com.spartangym.planes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "planes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPlan;

    @NotBlank(message = "El nombre del plan es obligatorio")
    @Column(nullable = false)
    private String nombrePlan;

    @NotBlank(message = "La descripcion del plan es obligatoria")
    @Column(nullable = false)
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    @Column(nullable = false)
    private Double precio;

    @NotNull(message = "La duracion en dias es obligatoria")
    @Min(value = 1, message = "La duracion debe ser de al menos 1 dia")
    @Column(nullable = false)
    private Integer duracionDias;
}