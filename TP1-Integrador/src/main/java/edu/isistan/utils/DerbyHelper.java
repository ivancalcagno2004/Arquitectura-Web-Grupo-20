package edu.isistan.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DerbyHelper {
    private Connection conn;
    public DerbyHelper(Connection conn){
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void closePsAndCommit(Connection conn, PreparedStatement ps) {
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

    public abstract void cargarTabla() throws SQLException;
    public abstract void populated() throws Exception;
}
