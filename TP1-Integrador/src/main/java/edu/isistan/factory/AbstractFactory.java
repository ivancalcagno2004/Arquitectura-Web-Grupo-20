package edu.isistan.factory;

import java.sql.SQLException;

import edu.isistan.dao.DAOCliente;
import edu.isistan.dao.DAOFactura;
import edu.isistan.dao.DAOFacturaProducto;
import edu.isistan.dao.DAOProducto;

public abstract class AbstractFactory {
    public static final int MYSQL_JDBC = 1;
    public static final int DERBY_JDBC = 2;

    public abstract DAOCliente getDaoCliente() throws SQLException;
    public abstract DAOProducto getDaoProducto() throws SQLException;
    public abstract DAOFactura getDaoFactura() throws SQLException;
    public abstract DAOFacturaProducto getDaoFacturaProducto() throws SQLException;

    public static AbstractFactory getDAOFactory(int factory) {
        switch (factory) {
            case MYSQL_JDBC : return MySQLDAOFactory.getInstance();
            case DERBY_JDBC: return DerbyDAOFactory.getInstance();
            default: return null;
        }
    }
}
