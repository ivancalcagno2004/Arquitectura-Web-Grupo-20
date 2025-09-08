package edu.isistan.factory;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.isistan.dao.DAOCliente;
import edu.isistan.dao.DAOFactura;
import edu.isistan.dao.DAOFacturaProducto;
import edu.isistan.dao.DAOProducto;

public class MySQLDAOFactory extends AbstractFactory {
    private static Connection conn;
    private static MySQLDAOFactory instance = null;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String PORT = System.getenv().getOrDefault("MYSQL_HOST_PORT", "3306");
    private static final String DATABASE = System.getenv().getOrDefault("DATABASE", "facturadoraDB");
    private static final String USER = System.getenv().getOrDefault("MYSQL_USER", "root");
    private static final String PASS = System.getenv().getOrDefault("MYSQL_USER", "MiPassUsuario123!");
    private static final String URI =  "jdbc:mysql://localhost:"+PORT+"/"+DATABASE;

    private MySQLDAOFactory(){}

    public static synchronized MySQLDAOFactory getInstance() {
        if (instance == null) {
            instance = new MySQLDAOFactory();
        }
        return instance;
    }

    public static Connection createConnection(){
        if(conn==null){
            return conn;
        }
        try{
            Class.forName(DRIVER).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            conn = DriverManager.getConnection(URI, USER, PASS);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DAOCliente getDaoCliente() throws SQLException {
        return new DAOCliente(conn);
    }

    @Override
    public DAOProducto getDaoProducto() throws SQLException {
        return new DAOProducto(conn);
    }

    @Override
    public DAOFactura getDaoFactura() throws SQLException {
        return new DAOFactura(conn);
    }

    @Override
    public DAOFacturaProducto getDaoFacturaProducto() throws SQLException {
        return new DAOFacturaProducto(conn);
    }
    

}
