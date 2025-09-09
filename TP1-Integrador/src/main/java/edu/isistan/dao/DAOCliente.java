package edu.isistan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import edu.isistan.dto.ClienteFacturacion;
import edu.isistan.entities.Cliente;
import edu.isistan.utils.CSVHelper;
import edu.isistan.utils.DerbyHelper;

public class DAOCliente extends DerbyHelper implements DAO<Cliente> {

    public DAOCliente(Connection conn){
        super(conn);
    }

    @Override
    public void cargarTabla() throws SQLException {
        String tablaCliente = 
        "CREATE TABLE Cliente ("+
            "idCliente INT NOT NULL," +
            "nombre VARCHAR(500) NOT NULL," +
            "email VARCHAR(150) NOT NULL," +
            "CONSTRAINT pk_idCliente PRIMARY KEY (idCliente)"+
        ")";
        this.getConn().prepareStatement(tablaCliente).execute();
        this.getConn().commit();
    }

    @Override
    public void populated() throws Exception {
        try{
            CSVHelper csvClientes = new CSVHelper("clientes.csv");
            for (CSVRecord record : csvClientes.getData()) {
                try{
                    Integer idCliente = Integer.parseInt(record.get("idCliente"));
                    String nombre = record.get("nombre");
                    String email = record.get("email");
                    Cliente c = new Cliente(idCliente, nombre, email);
                    this.insert(c);
                } catch(NumberFormatException e){
                    System.out.println("Error en el formato de numero en el registro: " + record.toString());
                }
            }
            System.out.println("Clientes insertados");
        } catch(SQLException e){
            e.printStackTrace();
        }
    }    

    @Override
    public int insert(Cliente c) throws Exception {
        String insert = "INSERT INTO Cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = this.getConn().prepareStatement(insert);
            ps.setInt(1, c.getIdCliente());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getEmail());
            if(ps.executeUpdate() == 0) throw new Exception("Fallo el insert del cliente: " + c.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePsAndCommit(this.getConn(), ps);
        }
        return 0;
    }

    @Override
    public boolean delete(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Cliente find(Integer pk) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'find'");
    }

    @Override
    public boolean update(Cliente dao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<Cliente> selectList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectList'");
    }

    public List<ClienteFacturacion> selectClientesMayorFacturacion() throws Exception {
        String select = 
            "SELECT c.idCliente, c.nombre, SUM(fp.cantidad * p.valor) AS facturacion " +
            "FROM Cliente c " +
            "JOIN Factura f ON f.idCliente = c.idCliente " +
            "JOIN Factura_Producto fp ON fp.idFactura = f.idFactura " +
            "JOIN Producto p ON p.idProducto = fp.idProducto " +
            "GROUP BY c.idCliente, c.nombre " +
            "ORDER BY facturacion DESC";
        PreparedStatement ps = null;
        List<ClienteFacturacion> clientesFacturacion = new java.util.ArrayList<>();
        try {
            ps = this.getConn().prepareStatement(select);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClienteFacturacion cf = new ClienteFacturacion(rs.getInt(1), rs.getString(2), rs.getDouble(3));
                clientesFacturacion.add(cf);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closePsAndCommit(getConn(), ps);
        }
        return clientesFacturacion;
    }
}
