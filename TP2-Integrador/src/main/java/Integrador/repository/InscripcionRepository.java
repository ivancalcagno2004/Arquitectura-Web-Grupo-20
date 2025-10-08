package Integrador.repository;

import java.util.List;

import Integrador.dto.CarrerasConInscriptosDTO;
import Integrador.dto.EstudianteEnCarreraXCiudadDTO;
import Integrador.dto.ReporteCarreraAnualDTO;
import Integrador.dto.InscripcionDTO;
import Integrador.model.Inscripcion;

public interface InscripcionRepository {
    void insert(Inscripcion inscripcion);
    InscripcionDTO getById(Integer id);
    List<InscripcionDTO> getInscripciones();
    List<CarrerasConInscriptosDTO> getCarrerasConInscriptos();
    List<EstudianteEnCarreraXCiudadDTO> getEstudiantesEnCarreraXCiudad(String carrera, String ciudad);
    List<ReporteCarreraAnualDTO> getReporteCarreraAnual();
}
