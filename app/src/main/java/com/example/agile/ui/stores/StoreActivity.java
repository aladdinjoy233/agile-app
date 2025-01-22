package com.example.agile.ui.stores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.widget.PopupMenu;

import com.example.agile.R;
import com.example.agile.databinding.ActivityStoreBinding;
import com.example.agile.models.Usuario;

public class StoreActivity extends AppCompatActivity implements onStoreDeleteListener, onStoreLeaveListener {

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

            //        Setear tiendas en el recycler (despues de obtener el usuario)
            vm.getTiendas().observe(this, tiendas -> {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
                binding.rvTiendas.setLayoutManager(gridLayoutManager);

                StoreAdapter adapter = new StoreAdapter(tiendas, getLayoutInflater(), user.getId(), this, this);
                binding.rvTiendas.setAdapter(adapter);
            });
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

    @Override
    public void onResume() {
        super.onResume();
        vm.obtenerTiendas();
    }

    @Override
    public void onDeleteStore(int tiendaId) {
        vm.eliminarTienda(tiendaId);
    }

    @Override
    public void onLeaveStore(int tiendaId) {
        vm.salirTienda(tiendaId);
    }
}