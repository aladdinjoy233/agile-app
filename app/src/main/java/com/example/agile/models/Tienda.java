package com.example.agile.models;

import java.io.Serializable;

public class Tienda implements Serializable {
    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private int duenioId;
    private Usuario duenio;

    public Tienda(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
}
