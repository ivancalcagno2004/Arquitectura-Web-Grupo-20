package edu.isistan.entities;

import org.apache.commons.csv.CSVRecord;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String email;
    public Cliente(int id, String nombre, String email) {
        this.idCliente = id;
        this.nombre = nombre;
        this.email = email;
    }
    public Cliente(CSVRecord record) {
        this.idCliente = Integer.parseInt(record.get("idCliente"));
        this.nombre = record.get("nombre");
        this.email = record.get("email");
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
