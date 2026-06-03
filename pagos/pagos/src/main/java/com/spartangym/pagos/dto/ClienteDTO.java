package com.spartangym.pagos.dto;

import lombok.Data;

@Data
public class ClienteDTO {

    private Integer idCliente;
    private String rut;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String estado;
    private String rol;
}