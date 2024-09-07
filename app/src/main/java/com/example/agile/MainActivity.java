package com.example.agile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.agile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);

        vm.getSession().observe(this, session -> {
            if (session) {
                binding.pbSession.setVisibility(View.VISIBLE);
                binding.btLogin.setVisibility(View.GONE);
                binding.btSignup.setVisibility(View.GONE);
            } else {
                binding.pbSession.setVisibility(View.GONE);
                binding.btLogin.setVisibility(View.VISIBLE);
                binding.btSignup.setVisibility(View.VISIBLE);
            }
        });

        binding.pbSession.setVisibility(View.VISIBLE);
        vm.verifySession();

//        Ir a los activities respectivos
        binding.btLogin.setOnClickListener(v -> vm.login());
        binding.btSignup.setOnClickListener(v -> vm.signup());
    }
}