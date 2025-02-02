package com.example.agile.models;

public class Producto {
    private int productoId;
    private int tiendaId;
    private String codigo;
    private String nombre;
    private float precio;
    private int stock;
    private int categoriaId;

    public Producto(int tiendaId, String codigo, String nombre, float precio, int stock, int categoriaId) {
        this.tiendaId = tiendaId;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoriaId = categoriaId;
    }

    public Producto(int productoId, int tiendaId, String codigo, String nombre, float precio, int stock, int categoriaId) {
        this.productoId = productoId;
        this.tiendaId = tiendaId;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoriaId = categoriaId;
    }

    public int getProductoId() { return productoId; }

    public int getTiendaId() { return tiendaId; }

    public String getCodigo() { return codigo; }

    public String getNombre() { return nombre; }

    public float getPrecio() { return precio; }

    public int getStock() { return stock; }

    public int getCategoriaId() { return categoriaId; }
}
