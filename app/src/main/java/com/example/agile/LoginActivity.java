package com.example.agile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.agile.databinding.ActivityLoginBinding;
import com.example.agile.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginViewModel.class);

        binding.btBack.setOnClickListener(v -> finish());

        // Parameters:                                   Email                                 Password
        binding.btLogin.setOnClickListener(v -> vm.login(binding.etEmail.getText().toString(), binding.etPassword.getText().toString()));
    }
}