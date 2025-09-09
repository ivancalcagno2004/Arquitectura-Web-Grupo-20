package edu.isistan.entities;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String email;
    public Cliente(int id, String nombre, String email) {
        this.idCliente = id;
        this.nombre = nombre;
        this.email = email;
    }
    public int getIdCliente() {
        return idCliente;
    }
    public String getNombre() {
        return nombre;
    }
    public String getEmail() {
        return email;
    }
    @Override
    public String toString() {
        return "Cliente [nombre=" + nombre + ", email=" + email + "]";
    }
}
