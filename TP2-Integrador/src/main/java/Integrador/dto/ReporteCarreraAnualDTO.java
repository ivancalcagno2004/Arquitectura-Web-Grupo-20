package Integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReporteCarreraAnualDTO {
    private String nombreCarrera;
    private Integer anio;
    private Long inscriptos;
    private Long egresados;
}
