package edu.isistan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import edu.isistan.entities.Factura;
import edu.isistan.utils.CSVHelper;

public class DAOFactura extends DAO<Factura> {

    public DAOFactura(Connection conn){
        super(conn);
    }

    @Override
    public void cargarTabla() throws SQLException {
        String tablaFactura = 
            "CREATE TABLE Factura ("+
                "idFactura INT NOT NULL,"+
                "idCliente INT,"+
                "CONSTRAINT pk_idFactura PRIMARY KEY (idFactura),"+
                "CONSTRAINT fk_Factura_Cliente FOREIGN KEY (idCliente) REFERENCES Cliente (idCliente)"+
            ")";
        this.getConn().prepareStatement(tablaFactura).execute();
        this.getConn().commit();
    }

    @Override
    public void populated() throws Exception {
        try{
            CSVHelper csvFacturas = new CSVHelper("facturas.csv");
            for (CSVRecord record : csvFacturas.getData()) {
                try{
                    Factura f = new Factura(record);
                    this.insert(f);
                } catch(NumberFormatException e){
                    System.out.println("Error en el formato de numero en el registro: " + record.toString());
                }
            }
            System.out.println("Facturas insertadas");
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public int insert(Factura f) throws Exception {
        String insert = "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = this.getConn().prepareStatement(insert);
            ps.setInt(1, f.getIdFactura());
            ps.setInt(2, f.getIdCliente());
            if(ps.executeUpdate() == 0) throw new Exception("Fallo el insert de la factura: " + f.toString());
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
    public Factura find(Integer pk) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'find'");
    }

    @Override
    public boolean update(Factura dao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<Factura> selectList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectList'");
    }

}
