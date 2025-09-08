package edu.isistan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import edu.isistan.entities.Cliente;
import edu.isistan.utils.CSVHelper;

public class DAOCliente extends DAO<Cliente> {

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
                    Cliente c = new Cliente(record);
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


}
