package edu.isistan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class DAO<T> {
    private Connection conn;
    public DAO(Connection conn){
        this.conn = conn;
    }

    public abstract void cargarTabla() throws SQLException;
    public abstract void populated() throws Exception;

    public abstract int insert(T dao) throws Exception;
    public abstract boolean delete(Integer id);
    public abstract T find(Integer pk);
    public abstract boolean update(T dao);
    public abstract List<T> selectList();

    public Connection getConn() {
        return conn;
    }

    protected void closePsAndCommit(Connection conn, PreparedStatement ps) {
        if (conn != null){
            try {
                if (ps != null) {
                    ps.close();
                }
                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
