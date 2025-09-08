package edu.isistan.entities;

public class Producto {
    private int idProducto;
    private String nombre;
    private float valor;
    public Producto(int id, String nombre, float valor) {
        this.idProducto = id;
        this.nombre = nombre;
        this.valor = valor;
    }
    public int getIdProducto() {
        return idProducto;
    }
    public String getNombre() {
        return nombre;
    }
    public float getValor() {
        return valor;
    }
    @Override
    public String toString() {
        return "Producto [id=" + idProducto + ", nombre=" + nombre + ", valor=" + valor + "]";
    }

}

