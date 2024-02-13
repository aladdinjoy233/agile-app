package com.example.agile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.agile.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private SignupViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(SignupViewModel.class);

        binding.btBack.setOnClickListener(v -> finish());

        // Parameters:                                     Name                                   Email                                 Password
        binding.btSignup.setOnClickListener(v -> vm.signup(binding.etNombre.getText().toString(), binding.etEmail.getText().toString(), binding.etPassword.getText().toString()));
    }
}