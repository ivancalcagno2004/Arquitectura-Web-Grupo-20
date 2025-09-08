package edu.isistan.entities;

import org.apache.commons.csv.CSVRecord;

public class FacturaProducto {
    private int idFactura;
    private int idProducto;
    private int cantidad;
    public FacturaProducto(int idFactura, int idProducto, int cantidad) {
        this.idFactura = idFactura;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }
    public FacturaProducto(CSVRecord record) {
        this.idFactura = Integer.parseInt(record.get("idFactura"));
        this.idProducto = Integer.parseInt(record.get("idProducto"));
        this.cantidad = Integer.parseInt(record.get("cantidad"));
    }
    public int getIdFactura() {
        return idFactura;
    }
    public int getIdProducto() {
        return idProducto;
    }
    public int getCantidad() {
        return cantidad;
    }
    @Override
    public String toString() {
        return "FacturaProducto [idFactura=" + idFactura + ", idProducto=" + idProducto + ", cantidad=" + cantidad + "]";
    }
}
