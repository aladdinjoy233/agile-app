package com.example.agile.ui.primary.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agile.R;
import com.example.agile.databinding.FragmentListUserBinding;

public class ListUserFragment extends Fragment {

    private FragmentListUserBinding binding;
    private ListUserViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ListUserViewModel.class);

        binding = FragmentListUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}