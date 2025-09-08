package edu.isistan.entities;

import org.apache.commons.csv.CSVRecord;

public class Factura {
    private int idFactura;
    private int idCliente;
    public Factura(int idFactura, int idCliente) {
        this.idFactura = idFactura;
        this.idCliente = idCliente;
    }
    public Factura(CSVRecord record) {
        this.idFactura = Integer.parseInt(record.get("idFactura"));
        this.idCliente = Integer.parseInt(record.get("idCliente"));
    }
    public int getIdFactura() {
        return idFactura;
    }
    public int getIdCliente() {
        return idCliente;
    }
    @Override
    public String toString() {
        return "Factura [idFactura=" + idFactura + ", idCliente=" + idCliente + "]";
    }
}
