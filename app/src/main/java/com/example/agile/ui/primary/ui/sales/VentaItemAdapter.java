package com.example.agile.ui.primary.ui.sales;

import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agile.databinding.CardSaleBinding;
import com.example.agile.models.VentaItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class VentaItemAdapter extends RecyclerView.Adapter<VentaItemAdapter.ViewHolder> {

    private ArrayList<VentaItem> items;
    private LayoutInflater inflater;
    private ItemListener listener;

    public interface ItemListener {
        void onButtonSumarClick(int productoId);
        void onButtonRestarClick(int productoId);
    }

    public VentaItemAdapter(ArrayList<VentaItem> items, LayoutInflater inflater, ItemListener listener) {
        this.items = items;
        this.inflater = inflater;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardSaleBinding binding = CardSaleBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VentaItemAdapter.ViewHolder holder, int position) {
        holder.binding.tvNombre.setText(items.get(position).getNombre());
        holder.binding.etCantidad.setText(items.get(position).getCantidad() + "");

//        Obtenemos el precio acumulado, multiplicando el precio unidad por la cantidad
        float precioAcumulado = items.get(position).getPrecioUnidad() * items.get(position).getCantidad();
        holder.binding.tvTotal.setText(formatPrecio(precioAcumulado));

        holder.binding.btSumar.setOnClickListener(v -> listener.onButtonSumarClick(items.get(position).getProductoId()));
        holder.binding.btRestar.setOnClickListener(v -> listener.onButtonRestarClick(items.get(position).getProductoId()));
    }

    @Override
    public int getItemCount() { return items.size(); }

    private String formatPrecio(float precio) {
        float f = Math.round(precio * 100f) / 100f;

        DecimalFormat df = new DecimalFormat("$#0.00");
        return df.format(f);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardSaleBinding binding;

        public ViewHolder(@NonNull CardSaleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
