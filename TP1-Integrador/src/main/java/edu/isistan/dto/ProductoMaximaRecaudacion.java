package edu.isistan.dto;

public class ProductoMaximaRecaudacion {
    private int id;
    private String nombre;
    private Double totalRecaudado;

    public ProductoMaximaRecaudacion(int id, String nombre, Double totalRecaudado) {
        this.id = id;
        this.nombre = nombre;
        this.totalRecaudado = totalRecaudado;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getTotalRecaudado() {
        return totalRecaudado;
    }

    @Override
    public String toString() {
        // Imprime el objeto en formato de tabla SQL
        return String.format("| %-5d | %-30s | %-15.2f |", id, nombre, totalRecaudado);
    }
}
