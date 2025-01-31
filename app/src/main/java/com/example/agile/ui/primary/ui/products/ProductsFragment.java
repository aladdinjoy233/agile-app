package com.example.agile.ui.primary.ui.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agile.R;
import com.example.agile.databinding.FragmentProductsBinding;
import com.example.agile.ui.primary.ui.settings.ListUserFragment;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    private FragmentProductsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ProductsViewModel vm = new ViewModelProvider(this).get(ProductsViewModel.class);

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
            FilterAdapter adapter = new FilterAdapter(categorias, inflater);
            binding.rvFilters.setAdapter(adapter);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}