package com.example.agile.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tienda implements Serializable {
    private int id;
    private String nombre;
    private String email;
    private String telefono;

    @SerializedName("dueñoId")
    private int duenioId;

    @SerializedName("dueño")
    private Usuario duenio;

    public Tienda(int id, String nombre, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public Tienda(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

//    Getters y setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public int getDuenioId() { return duenioId; }
    public String getTelefono() { return telefono; }
}
