package com.example.agile.ui.primary;

import android.os.Bundle;

import com.example.agile.R;
import com.example.agile.ui.primary.ui.dashboard.DashboardFragment;
import com.example.agile.ui.primary.ui.home.HomeFragment;
import com.example.agile.ui.primary.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.agile.databinding.ActivityPrimaryBinding;

public class PrimaryActivity extends AppCompatActivity {

    private ActivityPrimaryBinding binding;
    private PrimaryViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrimaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(PrimaryViewModel.class);
        clickProducts();

        binding.navProducts.setOnClickListener(v -> { clickProducts(); });

        binding.navSales.setOnClickListener(v -> { clickSales(); });

        binding.navSettings.setOnClickListener(v -> { clickSettings(); });

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

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_primary, new HomeFragment()).commit();
    }

    private void clickSales() {
        binding.navProducts.setSelected(false);
        binding.navSales.setSelected(true);
        binding.navSettings.setSelected(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_primary, new DashboardFragment()).commit();
    }

    private void clickSettings() {
        binding.navProducts.setSelected(false);
        binding.navSales.setSelected(false);
        binding.navSettings.setSelected(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_primary, new NotificationsFragment()).commit();
    }

}