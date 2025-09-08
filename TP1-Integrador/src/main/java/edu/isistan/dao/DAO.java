package edu.isistan.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    void cargarTabla() throws SQLException;
    void populated() throws Exception;
    int insert(T dao) throws Exception;
    boolean delete(Integer id);
    T find(Integer pk);
    boolean update(T dao);
    List<T> selectList();
}