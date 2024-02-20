package com.example.agile.ui.stores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.agile.R;
import com.example.agile.databinding.ActivityFormStoreBinding;

public class FormStoreActivity extends AppCompatActivity {
    ActivityFormStoreBinding binding;
    FormStoreViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFormStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(FormStoreViewModel.class);
    }
}