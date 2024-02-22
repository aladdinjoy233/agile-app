package com.example.agile.ui.stores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;

import com.example.agile.R;
import com.example.agile.databinding.ActivityStoreBinding;
import com.example.agile.models.Usuario;

public class StoreActivity extends AppCompatActivity {

    ActivityStoreBinding binding;
    StoreViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(StoreViewModel.class);

        vm.init((Usuario) getIntent().getSerializableExtra("usuario"));

        vm.getUsuario().observe(this, user -> {
            binding.tvNombre.setText(user.getNombre());
        });

        binding.btMas.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.dropdown_user, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.cerrar_sesion) {
                    vm.logout();
                    return true;
                }
                // else if (itemId == R.id.action_item2) {
                return false;
            });
            popupMenu.show();
        });

        binding.btNuevo.setOnClickListener(v -> {
            vm.nuevaTienda();
        });
    }
}