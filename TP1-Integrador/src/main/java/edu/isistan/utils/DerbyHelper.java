package edu.isistan.utils;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DerbyHelper {
    private static final String DB_NAME = "facturadoraDB";
    private Connection conn;

    public DerbyHelper() {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String uri = "jdbc:derby:"+DB_NAME+";create=true";

        try{
            Class.forName(driver).getDeclaredConstructor().newInstance();
        }catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException |
               NoSuchMethodException | SecurityException | ClassNotFoundException e){
            e.printStackTrace();
            System.exit(1);
        }

        try{
            conn = DriverManager.getConnection(uri);
            conn.setAutoCommit(false);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void dropTables() throws SQLException {
        String[] drops = new String[]{
            "DROP TABLE Factura_Producto",
            "DROP TABLE Factura",
            "DROP TABLE Cliente",
            "DROP TABLE Producto"
        };
        for (String d : drops) {
            try {
                this.conn.prepareStatement(d).execute();
                this.conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTables() throws SQLException {

        String tablaCliente = 
        "CREATE TABLE Cliente ("+
            "idCliente INT NOT NULL," +
            "nombre VARCHAR(500) NOT NULL," +
            "email VARCHAR(150) NOT NULL," +
            "CONSTRAINT pk_idCliente PRIMARY KEY (idCliente)"+
        ")";
        this.conn.prepareStatement(tablaCliente).execute();
        this.conn.commit();

        String tablaFactura = 
        "CREATE TABLE Factura ("+
            "idFactura INT NOT NULL,"+
            "idCliente INT,"+
            "CONSTRAINT pk_idFactura PRIMARY KEY (idFactura),"+
            "CONSTRAINT fk_Factura_Cliente FOREIGN KEY (idCliente) REFERENCES Cliente (idCliente)"+
        ")";
        this.conn.prepareStatement(tablaFactura).execute();
        this.conn.commit();

        String tablaProducto = 
        "CREATE TABLE Producto ("+
            "idProducto INT NOT NULL,"+
            "nombre VARCHAR(45) NOT NULL,"+
            "valor FLOAT NOT NULL,"+
            "CONSTRAINT pk_idProducto PRIMARY KEY (idProducto)"+
        ")";
        this.conn.prepareStatement(tablaProducto).execute();
        this.conn.commit();

        String tablaFacturaProducto =
        "CREATE TABLE Factura_Producto ("+
            "idProducto INT NOT NULL,"+
            "idFactura INT NOT NULL,"+
            "cantidad INT NOT NULL,"+
            "CONSTRAINT pk_idProducto_idFactura PRIMARY KEY (idProducto, idFactura),"+
            "CONSTRAINT fk_Producto_Factura FOREIGN KEY (idProducto) REFERENCES Producto (idProducto),"+
            "CONSTRAINT fk_Factura_Producto FOREIGN KEY (idFactura) REFERENCES Factura (idFactura)"+
        ")";
        this.conn.prepareStatement(tablaFacturaProducto).execute();
        this.conn.commit();
    }

}
