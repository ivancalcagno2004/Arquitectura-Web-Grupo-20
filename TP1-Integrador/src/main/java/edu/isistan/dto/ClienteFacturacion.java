package edu.isistan.dto;

public class ClienteFacturacion {
    private int id;
    private String nombre;
    private Double totalFacturado;

    public ClienteFacturacion(int id, String nombre, Double totalFacturado) {
        this.id = id;
        this.nombre = nombre;
        this.totalFacturado = totalFacturado;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getTotalFacturado() {
        return totalFacturado;
    }

    @Override
    public String toString() {
        // Imprime el objeto en formato de tabla SQL
        return String.format("| %-5d | %-30s | %-15.2f |", id, nombre, totalFacturado);
    }
}
