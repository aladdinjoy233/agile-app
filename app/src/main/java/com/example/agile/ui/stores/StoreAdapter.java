package com.example.agile.ui.stores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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
    private onStoreDeleteListener deleteListener;
    private onStoreLeaveListener leaveListener;
    private onStoreSelectListener selectListener;

    public StoreAdapter(ArrayList<Tienda> tiendas, LayoutInflater inflater, int userId, onStoreDeleteListener deleteListener, onStoreLeaveListener leaveListener, onStoreSelectListener selectListener) {
        this.tiendas = tiendas;
        this.inflater = inflater;
        this.userId = userId;
        this.deleteListener = deleteListener;
        this.leaveListener = leaveListener;
        this.selectListener = selectListener;
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

//            Agregamos click listener al botón de editar
            holder.binding.btEdit.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), FormStoreActivity.class);
                intent.putExtra("editMode", true);
                intent.putExtra("tiendaId", tienda.getId());
                v.getContext().startActivity(intent);
            });

//            Click listener, botón de eliminar
            holder.binding.btDelete.setOnClickListener(v -> mostrarDialogEliminar(v.getContext(), tienda));

        } else { // Si no es el dueño de la tienda
//            Ocultamos el botón de editar y eliminar
            holder.binding.btEdit.setVisibility(View.GONE);
            holder.binding.btDelete.setVisibility(View.GONE);

//            Agregamos click listener al botón de salir
            holder.binding.btSalir.setOnClickListener(v -> mostrarDialogSalir(v.getContext(), tienda));
        }

//        Agregamos click listener para abrir la tienda
        holder.binding.getRoot().setOnClickListener(v -> {
            if (selectListener != null) {
                selectListener.onSelectStore(tienda.getId());
            }
        });
    }

    @Override
    public int getItemCount() { return tiendas.size(); }

    private void mostrarDialogEliminar(Context context, Tienda tienda) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmar eliminación")
                .setMessage("¿Desea eliminar la tienda \"" + tienda.getNombre() + "\"?\n\nEsta acción eliminará todos los datos de la tienda.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    if (deleteListener != null) {
                        deleteListener.onDeleteStore(tienda.getId());
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert);

        AlertDialog dialog = builder.create();
        dialog.show();

//        Setear colores de los botónes (rojo para eliminar)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    private void mostrarDialogSalir(Context context, Tienda tienda) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmar salida")
                .setMessage("¿Desea salir de la tienda \"" + tienda.getNombre() + "\"?\n\nNo podrás volver hasta que te vuelvan a invitar.")
                .setPositiveButton("Salir", (dialog, which) -> {
                    if (leaveListener != null) {
                        leaveListener.onLeaveStore(tienda.getId());
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert);

        AlertDialog dialog = builder.create();
        dialog.show();

//        Setear colores de los botónes (rojo para eliminar)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardStoreBinding binding;

        public ViewHolder(@NonNull CardStoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
