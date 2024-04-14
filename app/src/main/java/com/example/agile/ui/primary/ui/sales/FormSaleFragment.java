package com.example.agile.ui.primary.ui.sales;

import androidx.core.content.ContextCompat;
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

    private BottomSheetBehavior<LinearLayout> bsbMenu;
    private BottomSheetBehavior<LinearLayout> bsbList;
    private FormSaleViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(FormSaleViewModel.class);

        binding = FragmentFormSaleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        bsbMenu = BottomSheetBehavior.from(binding.bottomSection);
        bsbList = BottomSheetBehavior.from(binding.itemList);

        configurarCallbacks();

        binding.btExpand.setOnClickListener(v -> vm.setIsMenuExpanded(bsbList.getState() == BottomSheetBehavior.STATE_COLLAPSED));

        vm.getIsMenuExpanded().observe(getViewLifecycleOwner(), isMenuExpanded -> {
            if (isMenuExpanded)
                expandirMenu();
            else
                colapsarMenu();
        });

        return root;
    }

    private void configurarCallbacks() {
        bsbList.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    binding.bottomSection.setBackgroundResource(R.drawable.bg_sales_bottom);
//                    binding.btExpand.setActivated(true);
                    vm.setIsMenuExpanded(true);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                    binding.bottomSection.setBackgroundResource(R.drawable.bg_sales);
//                    binding.btExpand.setActivated(false);
                    vm.setIsMenuExpanded(false);
                }
            }

            @Override public void onSlide(@NonNull View view, float v) {}
        });

        bsbMenu.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    expandirMenu();
                    vm.setIsMenuExpanded(true);
                }
            }

            @Override public void onSlide(@NonNull View view, float v) {}
        });
    }

    private void expandirMenu() {
        bsbList.setState(BottomSheetBehavior.STATE_EXPANDED);
        binding.bottomSection.setBackgroundResource(R.drawable.bg_sales_bottom);
        binding.btExpand.setActivated(true);
    }

    private void colapsarMenu() {
        bsbList.setState(BottomSheetBehavior.STATE_COLLAPSED);
        binding.bottomSection.setBackgroundResource(R.drawable.bg_sales);
        binding.btExpand.setActivated(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}