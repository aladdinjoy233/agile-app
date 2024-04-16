package com.example.agile.ui.primary.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.agile.R;
import com.example.agile.databinding.FragmentSettingsBinding;
import com.example.agile.ui.primary.ui.sales.FormSaleFragment;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel vm = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        Modificar datos de la tienda
        binding.btModificarTienda.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setReorderingAllowed(true);

            ft.replace(R.id.nav_host_fragment_activity_primary, FormStoreFragment.class, null)
                    .addToBackStack(null)
                    .commit();
        });

//        Manejar usuarios
        binding.btManejarUsuarios.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setReorderingAllowed(true);

            ft.replace(R.id.nav_host_fragment_activity_primary, ListUserFragment.class, null)
                    .addToBackStack(null)
                    .commit();
        });

//        Modal para eliminar la tienda (binding.btEliminarTienda)

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}