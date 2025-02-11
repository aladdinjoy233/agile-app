package com.example.agile.models;

import java.util.List;

public class Venta {
    private int id;
    private int usuarioId;
    private String fecha;
    private float tiendaId;
    private List<VentaItem> ventaItems;

    public Venta(int id, int usuarioId, String fecha, float tiendaId, List<VentaItem> ventaItems) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.tiendaId = tiendaId;
        this.ventaItems = ventaItems;
    }

    public String getFecha() {
        return fecha;
    }

    public List<VentaItem> getVentaItems() {
        return ventaItems;
    }
}
