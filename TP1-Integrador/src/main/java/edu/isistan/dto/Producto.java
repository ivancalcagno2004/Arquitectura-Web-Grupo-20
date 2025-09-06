package edu.isistan.dto;

public class Producto {
    private String nombre;
    private float valor;

    public Producto(int id, String nombre, float valor) {
        this.nombre = nombre;
        this.valor = valor;
    }
    public String getNombre() {
        return nombre;
    }
    public float getValor() {
        return valor;
    }
}
