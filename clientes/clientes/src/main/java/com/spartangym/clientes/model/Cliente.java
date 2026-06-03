package com.spartangym.clientes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El RUT es obligatorio")
    private String rut;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "Correo invalido")
    @Column(nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "La contrasena es obligatoria")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "El telefono no puede estar vacio")
    private String telefono;

    @Column(nullable = false)
    private String estado = "Activo";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

}
