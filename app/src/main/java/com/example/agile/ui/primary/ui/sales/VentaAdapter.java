package com.example.agile.ui.primary.ui.sales;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agile.databinding.CardFinishedSaleBinding;
import com.example.agile.databinding.CardFinishedSaleItemBinding;
import com.example.agile.models.Venta;
import com.example.agile.models.VentaItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.ViewHolder> {

    private List<Venta> ventas;
    private LayoutInflater inflater;

    public VentaAdapter(List<Venta> ventas, LayoutInflater inflater) {
        this.ventas = ventas;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardFinishedSaleBinding binding = CardFinishedSaleBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VentaAdapter.ViewHolder holder, int position) {
        Venta venta = ventas.get(position);

        holder.binding.rvItems.setLayoutManager(new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false));
        VentaItemAdapter adapter = new VentaItemAdapter(venta.getVentaItems(), inflater);
        holder.binding.rvItems.setAdapter(adapter);

        holder.binding.tvFecha.setText(venta.getFecha());
        holder.binding.tvPrecioTotal.setText(formatPrecio(venta.getTotal()));
    }

    @Override
    public int getItemCount() { return ventas.size(); }

    private static String formatPrecio(float precio) {
        float f = Math.round(precio * 100f) / 100f;

        DecimalFormat df = new DecimalFormat("$#0.00");
        return df.format(f);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardFinishedSaleBinding binding;

        public ViewHolder(@NonNull CardFinishedSaleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

//    Venta item adapter
    public static class VentaItemAdapter extends RecyclerView.Adapter<VentaItemAdapter.ViewHolder> {

        List<VentaItem> items;
        LayoutInflater inflater;

        public VentaItemAdapter(List<VentaItem> items, LayoutInflater inflater) {
            this.items = items;
            this.inflater = inflater;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CardFinishedSaleItemBinding binding = CardFinishedSaleItemBinding.inflate(inflater, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull VentaItemAdapter.ViewHolder holder, int position) {
            holder.binding.tvProducto.setText(items.get(position).getNombre());
            holder.binding.tvCantidad.setText("x" + items.get(position).getCantidad());

            float precio = items.get(position).getPrecioUnidad() * items.get(position).getCantidad();
            holder.binding.tvPrecio.setText(VentaAdapter.formatPrecio(precio));
        }

        @Override
        public int getItemCount() { return items.size(); }

        public class ViewHolder extends RecyclerView.ViewHolder {
                CardFinishedSaleItemBinding binding;
                public ViewHolder(@NonNull CardFinishedSaleItemBinding binding) {
                    super(binding.getRoot());
                    this.binding = binding;
                }
            }
        }
}
