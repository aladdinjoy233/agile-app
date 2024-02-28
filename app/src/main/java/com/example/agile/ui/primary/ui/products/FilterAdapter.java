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

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private ArrayList<String> filters;
    private LayoutInflater inflater;

    public FilterAdapter(ArrayList<String> filters, LayoutInflater inflater) {
        this.filters = filters;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFilterBinding binding = ItemFilterBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, int position) {
        holder.binding.tvFilter.setText(filters.get(position));
    }

    @Override
    public int getItemCount() { return filters.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFilterBinding binding;

        public ViewHolder(@NonNull ItemFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                binding.tvFilter.setSelected(!binding.tvFilter.isSelected());
            });
        }
    }
}
