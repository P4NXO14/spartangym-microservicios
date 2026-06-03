package com.spartangym.rutinas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDTO {

    private String campo;
    private String mensaje;
}