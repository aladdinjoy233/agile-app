package com.example.agile.ui.primary.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agile.databinding.FragmentFormStoreBinding;

public class FormStoreFragment extends Fragment {

    private FragmentFormStoreBinding binding;
    private FormStoreViewModel vm;
    private StoreEditedSharedViewModel storeEditedSharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(FormStoreViewModel.class);
        storeEditedSharedViewModel = new ViewModelProvider(requireActivity()).get(StoreEditedSharedViewModel.class);

        binding = FragmentFormStoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        binding.btFinish.setOnClickListener(v -> {
            vm.guardarTienda(binding.etNombre.getText().toString(), binding.etEmail.getText().toString(), binding.etPhone.getText().toString());
        });

        vm.getTienda().observe(getViewLifecycleOwner(), tienda -> {
            binding.etNombre.setText(tienda.getNombre());
            binding.etEmail.setText(tienda.getEmail());
            binding.etPhone.setText(tienda.getTelefono());
        });

        vm.getOperationCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (Boolean.TRUE.equals(completed)) {
                storeEditedSharedViewModel.setEdicionCompletada(true);
                getParentFragmentManager().popBackStack();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}