package com.example.agile.models;

import java.io.Serializable;

public class Categoria implements Serializable {
    private int categoriaId;
    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
    public Categoria(int categoriaId, String nombre) {
        this.categoriaId = categoriaId;
        this.nombre = nombre;
    }

    public int getCategoriaId() { return categoriaId; }
    public String getNombre() { return nombre; }
}
