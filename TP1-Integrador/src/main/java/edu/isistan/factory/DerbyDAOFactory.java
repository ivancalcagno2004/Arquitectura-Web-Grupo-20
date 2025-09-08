package edu.isistan.factory;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.isistan.dao.DAOCliente;
import edu.isistan.dao.DAOFactura;
import edu.isistan.dao.DAOFacturaProducto;
import edu.isistan.dao.DAOProducto;

public class DerbyDAOFactory extends AbstractFactory{
    private static Connection conn;
    private static DerbyDAOFactory instance = null;
    private static final String DATABASE = System.getenv().getOrDefault("DATABASE", "facturadoraDB");
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String URI =  "jdbc:derby:"+DATABASE+";create=true";

    private DerbyDAOFactory(){}

    public static synchronized DerbyDAOFactory getInstance() {
        if (instance == null) {
            instance = new DerbyDAOFactory();
            conn = createConnection();
        }
        return instance;
    }

    public static Connection createConnection(){
        try{
            Class.forName(DRIVER).getDeclaredConstructor().newInstance();
        }catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException |
               NoSuchMethodException | SecurityException | ClassNotFoundException e){
            e.printStackTrace();
            System.exit(1);
        }
        try{
            conn = DriverManager.getConnection(URI);
            conn.setAutoCommit(false);
        } catch(Exception e){
            e.printStackTrace();
        }
        return conn;
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
