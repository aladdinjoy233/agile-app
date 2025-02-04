package com.example.agile.ui.primary.ui.products;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.agile.R;
import com.example.agile.databinding.FragmentFormProductBinding;
import com.example.agile.databinding.FragmentListUserBinding;
import com.example.agile.models.Categoria;
import com.example.agile.ui.primary.ui.settings.ListUserViewModel;

import java.util.ArrayList;
import java.util.List;

public class FormProductFragment extends Fragment {

    private FragmentFormProductBinding binding;
    private FormProductViewModel vm;
    private ProductSharedViewModel sharedViewModel;
    private ArrayAdapter<String> categoriaAdapter;
    private List<Categoria> categoriaList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(FormProductViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(ProductSharedViewModel.class);

        binding = FragmentFormProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        Setup Spinner
        setupCategoriasSpinner();

        Bundle args = getArguments();
        boolean editMode;
//        Init ViewModel
        if (args != null) {
            editMode = args.getBoolean("editMode");
            vm.setProductId(args.getInt("productId"));
        } else {
            editMode = false;
        }

        vm.getEditMode().observe(getViewLifecycleOwner(), isEdit -> {
            if (isEdit) {
                binding.tvTitle.setText("Editar producto");
                binding.btFinish.setText("Guardar");
                binding.btDelete.setVisibility(View.VISIBLE);
            } else {
                binding.tvTitle.setText("Nuevo producto");
                binding.btFinish.setText("Crear");
            }
        });

        vm.getProducto().observe(getViewLifecycleOwner(), producto -> {
            binding.etCodigo.setText(producto.getCodigo());
            binding.etNombre.setText(producto.getNombre());
            binding.etPrecio.setText(String.valueOf(producto.getPrecio()));
            binding.etStock.setText(String.valueOf(producto.getStock()));

//            Setear categoria seleccionada
            if (producto.getCategoriaId() != 0) {
                int posicion = posicionCategoria(producto.getCategoriaId());
                if (posicion != -1) {
                    binding.spCategoria.setSelection(posicion);
                }
            }
        });

        vm.getCategorias().observe(getViewLifecycleOwner(), categorias -> {
            updateCategoriasSpinner(categorias);
        });

        vm.init(editMode);

        binding.btBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        vm.getOperationCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (Boolean.TRUE.equals(completed)) {
                sharedViewModel.triggerRefresh();
                getParentFragmentManager().popBackStack();
            }
        });

        binding.btFinish.setOnClickListener(v -> {
            float precio = (float) parseEditTextToDouble(binding.etPrecio);
            int stock = parseEditTextToInt(binding.etStock);
            vm.guardarProducto(binding.etCodigo.getText().toString(), binding.etNombre.getText().toString(), precio, stock);
        });

        binding.btDelete.setOnClickListener(v -> {
            dialogEliminarProducto();
        });


        return root;
    }

    private int parseEditTextToInt(EditText editText) {
        String text = editText.getText().toString().trim();

        if (text.isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            editText.setError("Debe ser un valor numérico");
            return 0;
        }
    }

    private double parseEditTextToDouble(EditText editText) {
        String text = editText.getText().toString().trim();

        if (text.isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            editText.setError("Debe ser un valor numérico");
            return 0.0;
        }
    }

    private void setupCategoriasSpinner() {
//        Crear adapter con "Select Categoria"
        categoriaAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>());
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spCategoria.setAdapter(categoriaAdapter);

//        Setup listener para la seleccion
        binding.spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Categoria selectedCategoria = categoriaList.get(position - 1);
                    vm.setSelectedCategoria(selectedCategoria);
                } else {
                    vm.setSelectedCategoria(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm.setSelectedCategoria(null);
            }
        });
    }

    private void updateCategoriasSpinner(List<Categoria> categorias) {
        categoriaList = categorias;

//        Preparamos los items del spinner
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Seleccionar categoria");
        for (Categoria categoria : categorias) {
            spinnerItems.add(categoria.getNombre());
        }

//        Actualizamos el adapter
        categoriaAdapter.clear();
        categoriaAdapter.addAll(spinnerItems);
        categoriaAdapter.notifyDataSetChanged();
    }

    private int posicionCategoria(int categoriaId) {
        if (categoriaId == 0) return -1;

        for (int i = 0; i < categoriaList.size(); i++) {
            if (categoriaList.get(i).getCategoriaId() == categoriaId) return i + 1;
        }
        return -1;
    }

    private void dialogEliminarProducto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar eliminación")
                .setMessage("¿Desea eliminar este producto?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    vm.eliminarProducto();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert);

        AlertDialog dialog = builder.create();
        dialog.show();

//        Setear colores de los botónes (rojo para eliminar)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}