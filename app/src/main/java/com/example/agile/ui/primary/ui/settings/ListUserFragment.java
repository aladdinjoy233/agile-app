package com.example.agile.ui.primary.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.agile.R;
import com.example.agile.databinding.FragmentListUserBinding;
import com.example.agile.models.Usuario;

public class ListUserFragment extends Fragment implements UserAdapter.OnUserDeleteListener {

    private FragmentListUserBinding binding;
    private ListUserViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ListUserViewModel.class);

        binding = FragmentListUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        binding.btNuevo.setOnClickListener(v -> {
            showEmailDialog();
        });

        vm.getUsuarios().observe(getViewLifecycleOwner(), tiendas -> {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            binding.rvUsuarios.setLayoutManager(gridLayoutManager);

            UserAdapter adapter = new UserAdapter(tiendas, inflater, this);
            binding.rvUsuarios.setAdapter(adapter);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_email_input, null);
        EditText emailInput = dialogView.findViewById(R.id.etEmail);

        builder.setTitle("Enter Email")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    vm.invitarUsuario(emailInput.getText().toString().trim());
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onDeleteUser(Usuario usuario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar eliminación")
                .setMessage("¿Desea eliminar el usuario \"" + usuario.getEmail() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    vm.eliminarUsuario(usuario);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert);

        AlertDialog dialog = builder.create();
        dialog.show();

//        Setear colores de los botónes (rojo para eliminar)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }
}