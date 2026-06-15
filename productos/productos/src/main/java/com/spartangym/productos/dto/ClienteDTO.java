package com.spartangym.productos.dto;

import lombok.Data;

@Data
public class ClienteDTO {

    private Integer idCliente;
    private String nombre;
    private String apellido;
    private String rut;
    private String correo;
    private String telefono;
    private String estado;
    private String rol;
}