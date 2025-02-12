package com.example.agile.models;

import androidx.versionedparcelable.VersionedParcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        try {
//                Parsear la fecha
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date fechaDate = inputFormat.parse(fecha);

//                Formatear la fecha
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd 'de' MMMM - HH:mm", new Locale("es", "ES"));
            return outputFormat.format(fechaDate);

        } catch (ParseException e) {
            e.printStackTrace();
            return fecha;
        }
    }

    public List<VentaItem> getVentaItems() {
        return ventaItems;
    }

    public float getTotal() {
        float total = 0;
        for (VentaItem item : ventaItems) {
            total += item.getPrecioUnidad() * item.getCantidad();
        }
        return total;
    }
}
