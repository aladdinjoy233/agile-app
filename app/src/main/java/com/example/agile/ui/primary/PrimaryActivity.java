package com.example.agile.ui.primary;

import android.os.Bundle;
import android.widget.PopupMenu;

import com.example.agile.R;
import com.example.agile.ui.primary.ui.sales.SalesFragment;
import com.example.agile.ui.primary.ui.products.ProductsFragment;
import com.example.agile.ui.primary.ui.settings.SettingsFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.agile.databinding.ActivityPrimaryBinding;
import com.example.agile.ui.primary.ui.settings.StoreEditedSharedViewModel;

public class PrimaryActivity extends AppCompatActivity {

    private ActivityPrimaryBinding binding;
    private PrimaryViewModel vm;
    private StoreEditedSharedViewModel storeEditedSharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrimaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(PrimaryViewModel.class);
        storeEditedSharedViewModel = new ViewModelProvider(this).get(StoreEditedSharedViewModel.class);

        clickProducts();

        binding.navProducts.setOnClickListener(v -> clickProducts());

        binding.navSales.setOnClickListener(v -> clickSales());

        binding.navSettings.setOnClickListener(v -> clickSettings());

        binding.btMas.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.dropdown_user, popupMenu.getMenu());

//            Hacer visible el item de cambiar de tienda
            popupMenu.getMenu().findItem(R.id.cambiar_tienda).setVisible(true);

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.cerrar_sesion) {
                    vm.logout();
                    return true;
                } else if (itemId == R.id.cambiar_tienda) {
                     vm.cambiarTienda();
                     return true;
                }
                return false;
            });
            popupMenu.show();
        });

        vm.getTienda().observe(this, tienda -> {
            binding.tvNombre.setText(tienda.getNombre());
        });

        storeEditedSharedViewModel.getEdicionCompletada().observe(this, completada -> {
            if (completada) {
                vm.obtenerTienda();
                storeEditedSharedViewModel.reset();
            }
        });

        /*

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_primary);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        */
    }

    private void clickProducts() {
        binding.navProducts.setSelected(true);
        binding.navSales.setSelected(false);
        binding.navSettings.setSelected(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_primary, new ProductsFragment()).commit();
    }

    private void clickSales() {
        binding.navProducts.setSelected(false);
        binding.navSales.setSelected(true);
        binding.navSettings.setSelected(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_primary, new SalesFragment()).commit();
    }

    private void clickSettings() {
        binding.navProducts.setSelected(false);
        binding.navSales.setSelected(false);
        binding.navSettings.setSelected(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_primary, new SettingsFragment()).commit();
    }

}