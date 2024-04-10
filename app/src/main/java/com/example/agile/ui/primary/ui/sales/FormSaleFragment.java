package com.example.agile.ui.primary.ui.sales;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.agile.R;
import com.example.agile.databinding.FragmentFormSaleBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class FormSaleFragment extends Fragment {

    private FragmentFormSaleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FormSaleViewModel vm = new ViewModelProvider(this).get(FormSaleViewModel.class);

        binding = FragmentFormSaleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        BottomSheetBehavior<LinearLayout> bsbMenu = BottomSheetBehavior.from(binding.bottomSection);
        BottomSheetBehavior<LinearLayout> bsbList = BottomSheetBehavior.from(binding.itemList);

        bsbList.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    Log.d("bottom sheet", "LIST EXPANDED");
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    Log.d("bottom sheet", "LIST COLLAPSED");
                }
            }

            @Override public void onSlide(@NonNull View view, float v) {}
        });

        bsbMenu.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    Log.d("bottom sheet", "MENU EXPANDED");
                    bsbList.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override public void onSlide(@NonNull View view, float v) {}
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}