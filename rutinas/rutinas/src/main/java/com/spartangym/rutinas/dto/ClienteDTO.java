package com.spartangym.rutinas.dto;

import lombok.Data;

@Data
public class ClienteDTO {

    private Integer idCliente;
    private String rut;
    private String nombreCompleto;
    private String estado;
    private String rol;
}