package Integrador.repository;

import java.util.List;

import Integrador.dto.CarreraDTO;
import Integrador.factory.JPAUtil;
import Integrador.model.Carrera;
import jakarta.persistence.EntityManager;

public class CarreraRepositoryImp implements CarreraRepository {

    @Override
    public void insert(Carrera carrera) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(carrera);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public Carrera getById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        Carrera carrera = null;
        try {
            carrera = em.find(Carrera.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return carrera;
    }

    @Override
    public List<CarreraDTO> getCarreras() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCarreras'");
    }

}