package com.example.agile.ui.stores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.agile.R;
import com.example.agile.databinding.ActivityFormStoreBinding;

public class FormStoreActivity extends AppCompatActivity {
    ActivityFormStoreBinding binding;
    FormStoreViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFormStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(FormStoreViewModel.class);

        vm.getEditMode().observe(this, editMode -> {
            if (editMode) {
                binding.tvTitle.setText("Editar tienda");
                binding.btFinish.setText("Guardar");
            } else {
                binding.tvTitle.setText("Nueva tienda");
                binding.btFinish.setText("Crear");
            }
        });

        /*
        vm.getTienda().observe(this, tienda -> {
            binding.etNombre.setText(tienda.getNombre());
            binding.etEmail.setText(tienda.getEmail());
            binding.etPhone.setText(tienda.getPhone());
        });
         */

        vm.init(getIntent().getBooleanExtra("editMode", false));

        binding.btBack.setOnClickListener(v -> finish());

        //                                                          Nombre                                 Email                                 Teléfono
        binding.btFinish.setOnClickListener(v -> { vm.guardarTienda(binding.etNombre.getText().toString(), binding.etEmail.getText().toString(), binding.etPhone.getText().toString()); });
    }
}