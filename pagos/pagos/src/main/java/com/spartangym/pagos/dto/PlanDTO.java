package com.spartangym.pagos.dto;

import lombok.Data;

@Data
public class PlanDTO {

    private Integer idPlan;
    private String nombrePlan;
    private Double precio;
    private Integer duracionDias;
}