package com.example.agile.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int id;
    private String nombre;
    private String email;
    private String password;
    private boolean esDuenio;
    private boolean esInvitado;

    public Usuario() {}

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

    public Usuario(int id, String nombre, String email, boolean esDuenio, boolean esInvitado) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.esDuenio = esDuenio;
        this.esInvitado = esInvitado;
    }

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public boolean getEsDuenio() { return esDuenio; }
    public boolean getEsInvitado() { return esInvitado; }
    public int getId() { return id; }
}
