package com.example.agile.ui.primary.ui.products;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agile.R;
import com.example.agile.databinding.FragmentProductsBinding;
import com.example.agile.ui.primary.ui.settings.ListUserFragment;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class ProductsFragment extends Fragment implements FilterAdapter.OnCategorySelectionListener {

    private FragmentProductsBinding binding;
    private ProductsViewModel vm;
    private ProductSharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ProductsViewModel.class);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(ProductSharedViewModel.class);

        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btNuevo.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setReorderingAllowed(true);

            ft.replace(R.id.nav_host_fragment_activity_primary, FormProductFragment.class, null)
                    .addToBackStack(null)
                    .commit();
        });

        vm.getCategorias().observe(getViewLifecycleOwner(), categorias -> {

//            If there are no categories in the list, hide the filter view
            if (categorias.isEmpty()) {
                binding.rvFilters.setVisibility(View.GONE);
                return;
            }

            binding.rvFilters.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
            FilterAdapter adapter = new FilterAdapter(categorias, inflater, this);
            binding.rvFilters.setAdapter(adapter);
        });

        vm.getProductosFiltrados().observe(getViewLifecycleOwner(), productos -> {
           binding.rvProductos.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
            ProductAdapter adapter = new ProductAdapter(productos, inflater, productId -> {

//                Creamos los bundle
                Bundle bundle = new Bundle();
                bundle.putBoolean("editMode", true);
                bundle.putInt("productId", productId);

//                Navegar al form
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setReorderingAllowed(true);

                Fragment formProductFragment = new FormProductFragment();
                formProductFragment.setArguments(bundle);

                ft.replace(R.id.nav_host_fragment_activity_primary, formProductFragment)
                        .addToBackStack(null)
                        .commit();

            });
            binding.rvProductos.setAdapter(adapter);
        });

//        Agregar un TextWatcher para el filtrado
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.btClear.setVisibility(s.toString().trim().isEmpty() ? View.GONE : View.VISIBLE);
                vm.setQuery(s.toString());
                vm.filtrarProductos();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btClear.setOnClickListener(v -> {
            binding.etSearch.setText("");
            binding.etSearch.clearFocus();
            hideKeyboard();
        });

//        Close the keyboard when they tap search
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });

//        Observe refresh trigger
        sharedViewModel.getRefreshTrigger().observe(getViewLifecycleOwner(), trigger -> {
            if (trigger) {
                vm.obtenerProductos();
                sharedViewModel.resetRefresh();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onCategorySelectionChanged(Set<Integer> selectedCategories) {
        vm.setCategoriasSeleccionadas(selectedCategories);
        vm.filtrarProductos();
    }
}