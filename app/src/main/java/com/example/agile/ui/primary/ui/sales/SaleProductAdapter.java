package com.example.agile.ui.primary.ui.sales;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agile.databinding.CardProductBinding;
import com.example.agile.models.Producto;
import com.example.agile.ui.primary.ui.products.ProductAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SaleProductAdapter extends RecyclerView.Adapter<SaleProductAdapter.ViewHolder> {

    private ArrayList<Producto> productos;
    private LayoutInflater inflater;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void OnProductClick(int productId);
    }

    public SaleProductAdapter(ArrayList<Producto> productos, LayoutInflater inflater, OnProductClickListener listener) {
        this.productos = productos;
        this.inflater = inflater;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardProductBinding binding = CardProductBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleProductAdapter.ViewHolder holder, int position) {
        holder.binding.tvProducto.setText(productos.get(position).getNombre());
        holder.binding.tvPrecio.setText(formatPrecio(productos.get(position).getPrecio()));
        holder.binding.tvStock.setText("Stock: " + productos.get(position).getStock());

        holder.binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.OnProductClick(productos.get(position).getProductoId());
            }
        });
    }

    @Override
    public int getItemCount() { return productos.size(); }

    private String formatPrecio(float precio) {
        float f = Math.round(precio * 100f) / 100f;

        DecimalFormat df = new DecimalFormat("$#0.00");
        return df.format(f);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardProductBinding binding;

        public ViewHolder(@NonNull CardProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
