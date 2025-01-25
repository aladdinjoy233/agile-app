package com.example.agile.ui.primary.ui.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agile.R;
import com.example.agile.databinding.CardUsuarioBinding;
import com.example.agile.models.Usuario;
import com.example.agile.ui.stores.StoreAdapter;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<Usuario> usuarios;
    private LayoutInflater inflater;
    private OnUserDeleteListener deleteListener;

    public UserAdapter(ArrayList<Usuario> usuarios, LayoutInflater inflater, OnUserDeleteListener deleteListener) {
        this.usuarios = usuarios;
        this.inflater = inflater;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardUsuarioBinding binding = CardUsuarioBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);

        if (usuario.getEsDuenio()) {
            holder.binding.btDelete.setVisibility(View.GONE);
        }

//        Check if the user has a name
        if (usuario.getNombre().isEmpty() && !usuario.getEsInvitado()) {
            holder.binding.tvNombre.setText("Usuario sin nombre");
        } else if (usuario.getEsInvitado()) {
            holder.binding.tvNombre.setText("Sin usuario");
            holder.binding.tvNombre.setTextColor(holder.itemView.getResources().getColor(R.color.secondaryText));
        } else if (usuario.getEsDuenio()) {
            holder.binding.tvNombre.setText(usuario.getNombre() + " (Dueño)");
        } else {
            holder.binding.tvNombre.setText(usuario.getNombre());
        }

        holder.binding.tvEmail.setText(usuario.getEmail());

        holder.binding.btDelete.setOnClickListener(v -> {
            deleteListener.onDeleteUser(usuario);
        });
    }

    @Override
    public int getItemCount() { return usuarios.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardUsuarioBinding binding;
        public ViewHolder(@NonNull CardUsuarioBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnUserDeleteListener {
        void onDeleteUser(Usuario usuario);
    }
}
