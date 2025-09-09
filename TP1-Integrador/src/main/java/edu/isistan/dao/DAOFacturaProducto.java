package edu.isistan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import edu.isistan.entities.FacturaProducto;
import edu.isistan.utils.CSVHelper;
import edu.isistan.utils.DerbyHelper;

public class DAOFacturaProducto extends DerbyHelper implements DAO<FacturaProducto> {
    public DAOFacturaProducto(Connection conn){
        super(conn);
    }

    @Override
    public void cargarTabla() throws SQLException {
        String tablaFacturaProducto =
        "CREATE TABLE Factura_Producto ("+
            "idProducto INT NOT NULL,"+
            "idFactura INT NOT NULL,"+
            "cantidad INT NOT NULL,"+
            "CONSTRAINT pk_idProducto_idFactura PRIMARY KEY (idProducto, idFactura),"+
            "CONSTRAINT fk_Producto_Factura FOREIGN KEY (idProducto) REFERENCES Producto (idProducto),"+
            "CONSTRAINT fk_Factura_Producto FOREIGN KEY (idFactura) REFERENCES Factura (idFactura)"+
        ")";
        this.getConn().prepareStatement(tablaFacturaProducto).execute();
        this.getConn().commit();
    }

    @Override
    public void populated() throws Exception {
        try {
            CSVHelper csvFacturaProducto = new CSVHelper("facturas-productos.csv");
            for (CSVRecord record : csvFacturaProducto.getData()) {
                try{
                    Integer idFactura = Integer.parseInt(record.get("idFactura"));
                    Integer idProducto = Integer.parseInt(record.get("idProducto"));
                    Integer cantidad = Integer.parseInt(record.get("cantidad"));
                    FacturaProducto fp = new FacturaProducto(idFactura, idProducto, cantidad);
                    this.insert(fp);
                } catch(NumberFormatException e){
                    System.out.println("Error en el formato de numero en el registro: " + record.toString());
                }
            }
            System.out.println("Factura_Producto insertados");
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public int insert(FacturaProducto fp) throws Exception {
        String insert = "INSERT INTO Factura_Producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = this.getConn().prepareStatement(insert);
            ps.setInt(1, fp.getIdFactura());
            ps.setInt(2, fp.getIdProducto());
            ps.setInt(3, fp.getCantidad());
            if(ps.executeUpdate() == 0) throw new Exception("Fallo el insert de la factura_producto: " + fp.toString());
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
    public FacturaProducto find(Integer pk) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'find'");
    }

    @Override
    public boolean update(FacturaProducto dao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<FacturaProducto> selectList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectList'");
    }
}
