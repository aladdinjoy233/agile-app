package com.example.agile.ui.primary.ui.products;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agile.R;
import com.example.agile.databinding.FragmentFormProductBinding;
import com.example.agile.databinding.FragmentListUserBinding;
import com.example.agile.ui.primary.ui.settings.ListUserViewModel;

public class FormProductFragment extends Fragment {

    private FragmentFormProductBinding binding;
    private FormProductViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(FormProductViewModel.class);

        binding = FragmentFormProductBinding.inflate(inflater, container, false);
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