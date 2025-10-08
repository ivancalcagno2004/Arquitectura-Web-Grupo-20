package Integrador.repository;

import java.util.ArrayList;
import java.util.List;

import Integrador.dto.CarrerasConInscriptosDTO;
import Integrador.dto.EstudianteEnCarreraXCiudadDTO;
import Integrador.dto.InscripcionDTO;
import Integrador.dto.ReporteCarreraAnualDTO;
import Integrador.factory.JPAUtil;
import Integrador.model.Inscripcion;
import jakarta.persistence.EntityManager;

public class InscripcionRepositoryImp implements InscripcionRepository {

    @Override
    public void insert(Inscripcion inscripcion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(inscripcion);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public InscripcionDTO getById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        InscripcionDTO dto = null;
        try {
            Inscripcion inscripcion = em.find(Inscripcion.class, id);
            dto = new InscripcionDTO(
                inscripcion.getId(),
                inscripcion.getEstudiante().getDni(),
                inscripcion.getCarrera().getId(),
                inscripcion.getInscripcion(),
                inscripcion.getGraduacion(),
                inscripcion.getAntiguedad()
            );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return dto;
    }

    @Override
    public List<CarrerasConInscriptosDTO> getCarrerasConInscriptos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<CarrerasConInscriptosDTO> resultados = null;
        try {
            String queryStr = "SELECT new Integrador.dto.CarrerasConInscriptosDTO(c.nombre, COUNT(i.id)) " +
                              "FROM Inscripcion i " +
                              "JOIN i.carrera c " +
                              "GROUP BY c.nombre " +
                              "ORDER BY COUNT(i.id) DESC";
            resultados = em.createQuery(queryStr, CarrerasConInscriptosDTO.class)
                           .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return resultados;
    }

    @Override
    public List<EstudianteEnCarreraXCiudadDTO> getEstudiantesEnCarreraXCiudad(String carrera, String ciudad){
        EntityManager em = JPAUtil.getEntityManager();
        List<EstudianteEnCarreraXCiudadDTO> resultados = null;
        try {
            String queryStr = "SELECT new Integrador.dto.EstudianteEnCarreraXCiudadDTO(e.dni, e.nombre, e.apellido, c.nombre, e.ciudad) " +
                              "FROM Inscripcion i " +
                              "JOIN i.estudiante e " +
                              "JOIN i.carrera c " +
                              "WHERE c.nombre = :carrera AND e.ciudad = :ciudad";
            resultados = em.createQuery(queryStr, EstudianteEnCarreraXCiudadDTO.class)
                           .setParameter("carrera", carrera)
                           .setParameter("ciudad", ciudad)
                           .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return resultados;
    }

    @Override
    public List<ReporteCarreraAnualDTO> getReporteCarreraAnual() {
        EntityManager em = JPAUtil.getEntityManager();
        // Usamos SQL nativo porque JPQL no soporta UNION
        String sql =
            "SELECT fj.nombre, fj.anio, SUM(fj.inscriptos) AS inscriptos, SUM(fj.egresados) AS egresados " +
            "FROM ( " +
            "  SELECT c.nombre AS nombre, i.inscripcion AS anio, COUNT(*) AS inscriptos, 0 AS egresados " +
            "  FROM INSCRIPCION i JOIN CARRERA c ON c.id = i.carrera " +
            "  GROUP BY c.nombre, i.inscripcion " +
            "  UNION " +
            "  SELECT c.nombre AS nombre, i.graduacion AS anio, 0 AS inscriptos, COUNT(*) AS egresados " +
            "  FROM INSCRIPCION i JOIN CARRERA c ON c.id = i.carrera " +
            "  GROUP BY c.nombre, i.graduacion HAVING anio IS NOT NULL " +
            ") fj " +
            "GROUP BY fj.nombre, fj.anio " +
            "ORDER BY fj.nombre ASC, fj.anio ASC";
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> queryList =  em.createNativeQuery(sql).getResultList();
            List<ReporteCarreraAnualDTO> report = new ArrayList<>();
            for (Object[] r : queryList) {
                report.add(new ReporteCarreraAnualDTO(
                    (String) r[0],
                    ((Number) r[1]).intValue(),
                    ((Number) r[2]).longValue(),
                    ((Number) r[3]).longValue()
                ));
            }
            return report;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            em.close();
        }
    }
    
    @Override
    public List<InscripcionDTO> getInscripciones() {
        EntityManager em = JPAUtil.getEntityManager();
        List<InscripcionDTO> resultados = null;
        try {
            String queryStr = "SELECT new Integrador.dto.InscripcionDTO(i.id, i.fecha, e.dni, e.nombre, e.apellido, c.nombre) " +
                              "FROM Inscripcion i " +
                              "JOIN i.estudiante e " +
                              "JOIN i.carrera c";
            resultados = em.createQuery(queryStr, InscripcionDTO.class)
                           .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return resultados;
    }
}

