package com.example.agile.models;

public class VentaItem {
    private int id;
    private int productoId;
    private int cantidad;
    private float precioUnidad;
    private String nombre;
    private int ventaId;

    public VentaItem(int productoId, int cantidad, float precioUnidad, String nombre) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnidad = precioUnidad;
        this.nombre = nombre;
    }

    public VentaItem(int id, int productoId, int cantidad, float precioUnidad, String nombre, int ventaId) {
        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnidad = precioUnidad;
        this.nombre = nombre;
        this.ventaId = ventaId;
    }

    public int getId() {
        return id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public float getPrecioUnidad() {
        return precioUnidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioUnidad(float precioUnidad) {
        this.precioUnidad = precioUnidad;
    }
}
