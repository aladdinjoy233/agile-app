package com.example.agile.ui.primary.ui.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agile.R;
import com.example.agile.databinding.ItemFilterBinding;
import com.example.agile.models.Categoria;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private ArrayList<Categoria> filters;
    private LayoutInflater inflater;
    private Set<Integer> selectedCategories = new HashSet<>();
    private OnCategorySelectionListener listener;

    public interface OnCategorySelectionListener {
        void onCategorySelectionChanged(Set<Integer> selectedCategories);
    }

    public FilterAdapter(ArrayList<Categoria> filters, LayoutInflater inflater, OnCategorySelectionListener listener) {
        this.filters = filters;
        this.inflater = inflater;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFilterBinding binding = ItemFilterBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, int position) {
        Categoria categoria = filters.get(position);
        holder.binding.tvFilter.setText(filters.get(position).getNombre());

//        Setear el estado del filtro
        holder.binding.tvFilter.setSelected(selectedCategories.contains(categoria.getCategoriaId()));

//        Toggle seleccion on click
        holder.binding.getRoot().setOnClickListener(v -> {
            toggleSelection(categoria.getCategoriaId());
            holder.binding.tvFilter.setSelected(selectedCategories.contains(categoria.getCategoriaId()));

//            Notificar listener
            if (listener != null) {
                listener.onCategorySelectionChanged(selectedCategories);
            }
        });
    }

    @Override
    public int getItemCount() { return filters.size(); }

    private void toggleSelection(int categoryId) {
        if (selectedCategories.contains(categoryId)) {
            selectedCategories.remove(categoryId);
        } else {
            selectedCategories.add(categoryId);
        }
    }

//    public Set<Integer> getSelectedCategories() {
//        return selectedCategories;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFilterBinding binding;

        public ViewHolder(@NonNull ItemFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//            binding.getRoot().setOnClickListener(v -> {
//                binding.tvFilter.setSelected(!binding.tvFilter.isSelected());
//            });
        }
    }
}
