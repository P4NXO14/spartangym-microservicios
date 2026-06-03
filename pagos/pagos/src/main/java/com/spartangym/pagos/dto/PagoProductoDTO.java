package com.spartangym.pagos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PagoProductoDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "La referencia de venta es obligatoria")
    private Integer referenciaId;

    @NotNull(message = "El monto cobrado es obligatorio")
    @Positive(message = "El monto cobrado debe ser mayor a 0")
    private Double montoCobrado;
}