package com.example.agile.ui.primary.ui.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agile.R;
import com.example.agile.databinding.FragmentSalesBinding;

public class SalesFragment extends Fragment {

    private FragmentSalesBinding binding;
    private SalesViewModel vm;
    private SaleSharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(SalesViewModel.class);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SaleSharedViewModel.class);

        binding = FragmentSalesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btNuevo.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setReorderingAllowed(true);

            ft.replace(R.id.nav_host_fragment_activity_primary, FormSaleFragment.class, null)
                    .addToBackStack(null)
                    .commit();
        });

//        RecyclerView de ventas
        vm.getVentas().observe(getViewLifecycleOwner(), ventas -> {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
            layoutManager.setStackFromEnd(true);
            binding.rvVentas.setLayoutManager(layoutManager);

            VentaAdapter adapter = new VentaAdapter(ventas, getLayoutInflater());
            binding.rvVentas.setAdapter(adapter);
        });

//        Observe refresh trigger
        sharedViewModel.getRefreshTrigger().observe(getViewLifecycleOwner(), trigger -> {
            if (trigger) {
                vm.obtenerVentas();
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
}