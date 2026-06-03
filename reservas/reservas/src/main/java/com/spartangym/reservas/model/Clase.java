package com.spartangym.reservas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "clases")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idClase;

    @NotBlank(message = "El nombre de la clase no puede estar vacio")
    private String nombreClase;

    @NotNull(message = "Los cupos totales no pueden ser nulos")
    @Positive(message = "Los cupos totales deben ser positivos")
    private Integer cuposTotales;

    @NotNull(message = "Los cupos disponibles no pueden ser nulos")
    @PositiveOrZero(message = "Los cupos disponibles deben ser positivos")
    private Integer cuposDisponibles;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate fecha;

    @NotNull(message = "La hora no puede ser nula")
    private LocalTime hora;
}