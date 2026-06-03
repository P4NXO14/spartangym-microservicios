package com.spartangym.productos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoProductoDTO {

    private Integer idCliente;
    private Integer referenciaId;
    private Double montoCobrado;
}