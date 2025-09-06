package edu.isistan.dto;

public class Factura {
    private int idCliente;
    public Factura(int idFactura, int idCliente, String fecha) {
        this.idCliente = idCliente;
    }
    public int getIdCliente() {
        return idCliente;
    }
}
