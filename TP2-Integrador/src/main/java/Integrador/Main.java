package Integrador;

import java.nio.file.Path;
import java.util.Scanner;

import Integrador.dto.CarreraDTO;
import Integrador.dto.CarrerasConInscriptosDTO;
import Integrador.dto.EstudianteDTO;
import Integrador.dto.EstudianteEnCarreraXCiudadDTO;
import Integrador.dto.InscripcionDTO;
import Integrador.dto.ReporteCarreraAnualDTO;
import Integrador.model.Carrera;
import Integrador.model.Estudiante;
import Integrador.model.Inscripcion;
import Integrador.repository.CarreraRepository;
import Integrador.repository.CarreraRepositoryImp;
import Integrador.repository.EstudianteRepository;
import Integrador.repository.EstudianteRepositoryImp;
import Integrador.repository.InscripcionRepository;
import Integrador.repository.InscripcionRepositoryImp;
import Integrador.utils.csv.CsvReader;
import Integrador.utils.csv.CsvCarrerasReader;
import Integrador.utils.csv.CsvEstudiantesReader;
import Integrador.utils.csv.CsvInscripcionReader;
import Integrador.utils.populated.Populated;
import Integrador.utils.populated.PopulatedCarreras;
import Integrador.utils.populated.PopulatedEstudiantes;
import Integrador.utils.populated.PopulatedInscripciones;

public class Main {
    private static final CarreraRepository cr = new CarreraRepositoryImp();
    private static final EstudianteRepository er = new EstudianteRepositoryImp();
    private static final InscripcionRepository ir = new InscripcionRepositoryImp();
    private static final String CSV_DIR = "TP2-Integrador/src/main/resources/";
    public static void main(String[] args) throws Exception {
        CsvReader<EstudianteDTO> cer = new CsvEstudiantesReader(Path.of(CSV_DIR + "estudiantes.csv"));
        CsvReader<CarreraDTO> ccr = new CsvCarrerasReader(Path.of(CSV_DIR + "carreras.csv"));
        CsvReader<InscripcionDTO> cir = new CsvInscripcionReader(Path.of(CSV_DIR + "estudianteCarrera.csv"));

        Populated<EstudianteDTO> estudiantesData = new PopulatedEstudiantes();
        estudiantesData.poblar(cer.getData());

        Populated<CarreraDTO> carrerasData = new PopulatedCarreras();
        carrerasData.poblar(ccr.getData());

        Populated<InscripcionDTO> inscripcionesData = new PopulatedInscripciones();
        inscripcionesData.poblar(cir.getData()); 

        // 2.a) Dar de alta un estudiante.
        Estudiante nuevoEstudiante = new Estudiante();
        nuevoEstudiante.setDni(40373769);
        nuevoEstudiante.setLu(250520);
        nuevoEstudiante.setNombre("Ulises");
        nuevoEstudiante.setApellido("Palazzo");
        nuevoEstudiante.setGenero("Macho");
        nuevoEstudiante.setCiudad("Rauch");
        er.insert(nuevoEstudiante);

        // 2.b) Matricular un estudiante a una carrera.
        Carrera tudai = cr.getById(1);
        Inscripcion nuevaMatricula = new Inscripcion();
        nuevaMatricula.setEstudiante(nuevoEstudiante);
        nuevaMatricula.setCarrera(tudai);
        nuevaMatricula.setInscripcion(2025);
        nuevaMatricula.setAntiguedad(2);
        ir.insert(nuevaMatricula);

        // 2.c) Recuperar todos los estudiantes, y especificar algún criterio de ordenamiento simple.
        System.out.println("Seleccione un criterio de ordenamiento para el Estudiante (nombre, apellido, edad, genero, ciudad, lu): ");
        System.out.println("1. Nombre");
        System.out.println("2. Apellido");
        System.out.println("3. Edad");
        System.out.println("4. Genero");
        System.out.println("5. Ciudad");
        System.out.println("6. LU");
        // Leer opción del usuario (1-6)
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;
        while (true) {
            System.out.print("Ingrese una opción (1-6): ");
            String entrada = scanner.nextLine();
            try {
                opcion = Integer.parseInt(entrada.trim());
                if (opcion >= 1 && opcion <= 6) {
                    break;
                }
            } catch (NumberFormatException ignored) {}
            System.out.println("Opción inválida. Intente nuevamente.");
        }
        scanner.close();
        String atributo = switch (opcion) {
            case 1 -> "nombre";
            case 2 -> "apellido";
            case 3 -> "edad";
            case 4 -> "genero";
            case 5 -> "ciudad";
            case 6 -> "lu";
            default -> "nombre";
        };
        String orden = "ASC"; // Orden ascendente
        System.out.println("Estudiantes ordenados por " + atributo + " en orden " + orden + ":");

        String header2 = String.format("| %-15s | %-12s | %-15s | %-15s | %-15s | %-20s | %-10s |", "DNI", "NOMBRE", "APELLIDO", "EDAD", "GENERO", "CIUDAD", "LU");
        System.out.println(header2);
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        for(EstudianteDTO e : er.getEstudiantesConOrden(atributo, orden)){
            System.out.println(String.format("| %-15s | %-12s | %-15s | %-15s | %-15s | %-20s | %-10s |",
                e.getDni(), e.getNombre(), e.getApellido(), e.getEdad(), e.getGenero(), e.getCiudad(), e.getLu()));
        };

        // 2.d) Recuperar un estudiante, en base a su número de libreta universitaria.
        int luBuscado = 250520;
        EstudianteDTO estudianteEncontrado = null;
        estudianteEncontrado = er.getByLu(luBuscado);
        if (estudianteEncontrado != null) {
            System.out.println("\nEstudiante encontrado con LU " + luBuscado + ":");
            String header3 = String.format("| %-15s | %-12s | %-15s | %-15s | %-15s | %-20s | %-10s |", "DNI", "NOMBRE", "APELLIDO", "EDAD", "GENERO", "CIUDAD", "LU");
            System.out.println(header3);
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
            System.out.println(String.format("| %-15s | %-12s | %-15s | %-15s | %-15s | %-20s | %-10s |",
                estudianteEncontrado.getDni(), estudianteEncontrado.getNombre(), estudianteEncontrado.getApellido(), estudianteEncontrado.getEdad(), estudianteEncontrado.getGenero(), estudianteEncontrado.getCiudad(), estudianteEncontrado.getLu()));
        } else {
            System.out.println("No se encontró ningún estudiante con LU " + luBuscado);
        }

        // 2.e) Recuperar todos los estudiantes, en base a su género.
        String generoBuscado = "Masculino";
        System.out.println("\nEstudiantes con género " + generoBuscado + ":");
        String header4 = String.format("| %-15s | %-12s | %-15s | %-15s | %-15s | %-20s | %-10s |", "DNI", "NOMBRE", "APELLIDO", "EDAD", "GENERO", "CIUDAD", "LU");
        System.out.println(header4);
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        for(EstudianteDTO e : er.getEstudiantesPorGenero(generoBuscado)){
            System.out.println(String.format("| %-15s | %-12s | %-15s | %-15s | %-15s | %-20s | %-10s |",
                e.getDni(), e.getNombre(), e.getApellido(), e.getEdad(), e.getGenero(), e.getCiudad(), e.getLu()));
        };

        // 2.f) Recuperar las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos.
        System.out.println("\nCarreras con cantidad de inscriptos:");
        String header5 = String.format("| %-30s | %-20s |", "NOMBRE CARRERA", "CANTIDAD INSCRIPTOS");
        System.out.println(header5);
        System.out.println("---------------------------------------------------------");
        for(CarrerasConInscriptosDTO c : ir.getCarrerasConInscriptos()){
            System.out.println(String.format("| %-30s | %-20s |",
                c.getNombreCarrera(), c.getCantidadInscriptos()));
        };

        // 2.g) Recuperar los estudiantes de una determinada carrera, filtrado por ciudad de residencia.
        String ciudadBuscada = "Rauch";
        String carreraBuscada = "TUDAI";
        System.out.println("\nEstudiantes de la carrera " + carreraBuscada + " en " + ciudadBuscada + ":");
        String header6 = String.format("| %-15s | %-12s | %-15s | %-15s | %-15s |", "DNI", "NOMBRE", "APELLIDO", "CARRERA", "CIUDAD");
        System.out.println(header6);
        System.out.println("----------------------------------------------------------------------------------------");
        for(EstudianteEnCarreraXCiudadDTO e : ir.getEstudiantesEnCarreraXCiudad(carreraBuscada, ciudadBuscada)){
            System.out.println(String.format("| %-15s | %-12s | %-15s | %-15s | %-15s |",
                e.getDni(), e.getNombre(), e.getApellido(), e.getCarrera(), e.getCiudad()));
        };

        // 3) Generar un reporte de las carreras, que para cada carrera incluya información de los inscriptos y egresados por año. Se deben ordenar las carreras alfabéticamente, y presentar los años de manera cronológica.
        System.out.println("\nReporte anual de carreras:");
        String header7 = String.format("| %-30s | %-10s | %-20s | %-20s |", "NOMBRE CARRERA", "AÑO", "CANTIDAD INSCRIPTOS", "CANTIDAD EGRESADOS");
        System.out.println(header7);
        System.out.println("-----------------------------------------------------------------------------------------------");
        for(ReporteCarreraAnualDTO r : ir.getReporteCarreraAnual()){
            System.out.println(String.format("| %-30s | %-10s | %-20s | %-20s |",
                r.getNombreCarrera(), r.getAnio(), r.getInscriptos(), r.getEgresados()));
        };
    }
}
