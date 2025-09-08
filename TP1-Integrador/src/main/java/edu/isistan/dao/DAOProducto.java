package edu.isistan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import edu.isistan.dto.ProductoMaximaRecaudacion;
import edu.isistan.entities.Producto;
import edu.isistan.utils.CSVHelper;
import edu.isistan.utils.DerbyHelper;

public class DAOProducto extends DerbyHelper implements DAO<Producto> {
    public DAOProducto(Connection conn){
        super(conn);
    }

    @Override
    public void cargarTabla() throws SQLException {
        String tablaProducto = 
        "CREATE TABLE Producto ("+
            "idProducto INT NOT NULL,"+
            "nombre VARCHAR(45) NOT NULL,"+
            "valor FLOAT NOT NULL,"+
            "CONSTRAINT pk_idProducto PRIMARY KEY (idProducto)"+
        ")";
        this.getConn().prepareStatement(tablaProducto).execute();
        this.getConn().commit();
    }

    @Override
    public void populated() throws Exception {
        try {
            CSVHelper csvProductos = new CSVHelper("productos.csv");
            for (CSVRecord record : csvProductos.getData()) {
                try{
                    Integer idProducto = Integer.parseInt(record.get("idProducto"));
                    String nombre = record.get("nombre");
                    Float valor = Float.parseFloat(record.get("valor"));
                    Producto p = new Producto(idProducto, nombre, valor);
                    this.insert(p);
                } catch(NumberFormatException e){
                    System.out.println("Error en el formato de numero en el registro: " + record.toString());
                }
            }
            System.out.println("Productos insertados");
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public int insert(Producto p) throws Exception {
        String insert = "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = this.getConn().prepareStatement(insert);
            ps.setInt(1, p.getIdProducto());
            ps.setString(2, p.getNombre());
            ps.setFloat(3, p.getValor());
            if(ps.executeUpdate() == 0) throw new Exception("Fallo el insert del producto: " + p.toString());
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
    public Producto find(Integer pk) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'find'");
    }

    @Override
    public boolean update(Producto dao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<Producto> selectList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectList'");
    }

    public ProductoMaximaRecaudacion selectProductoMayorRecaudacion() throws Exception{
        PreparedStatement ps = null;
        ProductoMaximaRecaudacion p = null;
        try {
            String select =
                "SELECT p.idProducto, p.nombre, SUM(fp.cantidad * p.valor) AS recaudacion " +
                "FROM Producto p " +
                "JOIN Factura_Producto fp ON fp.idProducto = p.idProducto " +
                "GROUP BY p.idProducto, p.nombre " +
                "ORDER BY recaudacion DESC " +
                "FETCH FIRST 1 ROW ONLY";
            ps = this.getConn().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                p = new ProductoMaximaRecaudacion(rs.getInt(1), rs.getString(2), rs.getDouble(3));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closePsAndCommit(getConn(), ps);
        }
        return p;
    }

}
