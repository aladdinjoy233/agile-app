package com.example.agile.ui.stores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agile.databinding.CardStoreBinding;
import com.example.agile.models.Tienda;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private ArrayList<Tienda> tiendas;
    private LayoutInflater inflater;
    private int userId;

    public StoreAdapter(ArrayList<Tienda> tiendas, LayoutInflater inflater, int userId) {
        this.tiendas = tiendas;
        this.inflater = inflater;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardStoreBinding binding = CardStoreBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tienda tienda = tiendas.get(position);

        holder.binding.tvNombre.setText(tienda.getNombre());

        if (tienda.getEmail().isEmpty()) {
            holder.binding.tvEmail.setVisibility(View.GONE);
        } else {
            holder.binding.tvEmail.setText(tienda.getEmail());
        }

        if (tienda.getDuenioId() == userId) { // Si es el dueño de la tienda
//            Ocultamos el botón de salir
            holder.binding.btSalir.setVisibility(View.GONE);
        } else { // Si no es el dueño de la tienda
//            Ocultamos el botón de editar y eliminar
            holder.binding.btEdit.setVisibility(View.GONE);
            holder.binding.btDelete.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() { return tiendas.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardStoreBinding binding;

        public ViewHolder(@NonNull CardStoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
