package com.example.agile.ui.primary.ui.products;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agile.databinding.CardProductBinding;
import com.example.agile.databinding.ItemFilterBinding;
import com.example.agile.models.Categoria;
import com.example.agile.models.Producto;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Producto> productos;
    private LayoutInflater inflater;

    public ProductAdapter(ArrayList<Producto> productos, LayoutInflater inflater) {
        this.productos = productos;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardProductBinding binding = CardProductBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        holder.binding.tvProducto.setText(productos.get(position).getNombre());
        holder.binding.tvPrecio.setText("$" + productos.get(position).getPrecio());
        holder.binding.tvStock.setText("Stock: " + productos.get(position).getStock());
    }

    @Override
    public int getItemCount() { return productos.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardProductBinding binding;

        public ViewHolder(@NonNull CardProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
