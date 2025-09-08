package edu.isistan;

import java.sql.SQLException;

import edu.isistan.dao.DAOCliente;
import edu.isistan.dao.DAOFactura;
import edu.isistan.dao.DAOFacturaProducto;
import edu.isistan.dao.DAOProducto;
import edu.isistan.dto.ClienteFacturacion;
import edu.isistan.factory.AbstractFactory;

public class Main {
    private static DAOCliente daoC;
    private static DAOProducto daoP;
    private static DAOFactura daoF;
    private static DAOFacturaProducto daoFP;
    private static AbstractFactory derbyFactory = AbstractFactory.getDAOFactory(AbstractFactory.DERBY_JDBC);
    public static void main(String[] args) throws Exception {
        //instanciarDaos();
        //crearTablas();
        //populated();
        System.out.println("Producto con mayor recaudacion:");
        String header1 = String.format("| %-5s | %-30s | %-15s |", "id", "nombre", "totalRecaudado");
        System.out.println(header1);
        System.out.println("-".repeat(header1.length()));
        System.out.println(derbyFactory.getDaoProducto().selectProductoMayorRecaudacion());

        System.out.println("\nClientes con mayor facturacion:");
        String header2 = String.format("| %-5s | %-30s | %-15s |", "id", "nombre", "totalFacturado");
        System.out.println(header2);
        System.out.println("-".repeat(header2.length()));
        for(ClienteFacturacion cf : derbyFactory.getDaoCliente().selectClientesMayorFacturacion()){
            System.out.println(cf);
        }

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