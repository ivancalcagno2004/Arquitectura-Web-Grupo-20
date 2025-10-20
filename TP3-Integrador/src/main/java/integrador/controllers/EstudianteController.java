package integrador.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import integrador.dto.estudiante.EstudianteRequestDTO;
import integrador.dto.estudiante.EstudianteResponseDTO;
import integrador.services.EstudianteService;
import integrador.services.exceptions.BadRequestException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("estudiantes")
public class EstudianteController {
    @Autowired
    private EstudianteService estudianteService;

    // a) dar de alta un estudiante
    @PostMapping("")
    public ResponseEntity<?> saveEstudiante(@Valid @RequestBody EstudianteRequestDTO e){
        try {
            EstudianteResponseDTO estudianteGuardado = this.estudianteService.save(e);
            return ResponseEntity.status(HttpStatus.CREATED).body(estudianteGuardado);
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // c) recuperar todos los estudiantes, y especificar algún criterio de ordenamiento simple.
    @GetMapping("ordenado")
    public ResponseEntity<?> getEstudiantesOrdenados(
            @RequestParam String orderBy,
            @RequestParam(required = false, defaultValue = "ASC") String direction) {
        try {
            List<EstudianteResponseDTO> estudiantes = this.estudianteService.findAllOrdered(orderBy, direction);
            return ResponseEntity.ok(estudiantes);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // d) recuperar un estudiante, en base a su número de libreta universitaria.
    @GetMapping("lu/{lu}")
    public ResponseEntity<?> findByLu(@PathVariable Integer lu) {
        try {
            return ResponseEntity.ok(this.estudianteService.findByLu(lu));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // e) recuperar todos los estudiantes, en base a su género.
    @GetMapping("genero/{genero}")
    public ResponseEntity<?> findByGenero(@PathVariable String genero) {
        try {
            List<EstudianteResponseDTO> estudiantes = this.estudianteService.findByGenero(genero);
            return ResponseEntity.ok(estudiantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<List<EstudianteResponseDTO>> findEstudiantes(){
        try{
            return ResponseEntity.ok(this.estudianteService.findAll());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(this.estudianteService.findById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // g) recuperar los estudiantes de una determinada carrera, filtrado por ciudad de residencia.
    @GetMapping("carrera/{carreraId}")
    public ResponseEntity<?> findByCarreraAndCiudad(
        @PathVariable Long carreraId,
        @RequestParam(required = false) String ciudad
    ) {
        try {
            List<EstudianteResponseDTO> estudiantes = this.estudianteService.findByCarreraAndCiudad(carreraId, ciudad);
            return ResponseEntity.ok(estudiantes);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
