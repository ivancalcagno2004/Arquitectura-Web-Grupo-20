package edu.isistan;

import java.sql.SQLException;

import edu.isistan.dao.DAOCliente;
import edu.isistan.dao.DAOFactura;
import edu.isistan.dao.DAOFacturaProducto;
import edu.isistan.dao.DAOProducto;
import edu.isistan.factory.AbstractFactory;

public class Main {
    private static DAOCliente daoC;
    private static DAOProducto daoP;
    private static DAOFactura daoF;
    private static DAOFacturaProducto daoFP;
    private static AbstractFactory derbyFactory = AbstractFactory.getDAOFactory(AbstractFactory.DERBY_JDBC);
    public static void main(String[] args) throws Exception {
        instanciarDaos();
        crearTablas();
        populated();

    }

    public static void instanciarDaos() throws SQLException {
        daoC = derbyFactory.getDaoCliente();
        daoF = derbyFactory.getDaoFactura();
        daoP = derbyFactory.getDaoProducto();
        daoFP = derbyFactory.getDaoFacturaProducto();
    }

    public static void crearTablas() throws SQLException {
        daoC.cargarTabla();
        daoF.cargarTabla();
        daoP.cargarTabla();
        daoFP.cargarTabla();
    }

    public static void populated() throws Exception{
        daoC.populated();
        daoF.populated();
        daoP.populated();
        daoFP.populated();
    }


}