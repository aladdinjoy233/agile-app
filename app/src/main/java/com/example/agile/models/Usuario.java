package com.example.agile.models;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int id;
    private String nombre;
    private String email;
    private String password;

    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }
    public Usuario(int id, String nombre, String email, String password) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
}
