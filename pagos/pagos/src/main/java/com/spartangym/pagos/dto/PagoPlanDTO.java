package com.spartangym.pagos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagoPlanDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "El ID del plan es obligatorio")
    private Integer idPlan;
}