package com.example.agile.ui.primary.ui.sales;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.agile.R;
import com.example.agile.databinding.FragmentFormSaleBinding;

public class FormSaleFragment extends Fragment {

    private FragmentFormSaleBinding binding;
    private GestureDetector gestureDetector;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FormSaleViewModel vm = new ViewModelProvider(this).get(FormSaleViewModel.class);

        gestureDetector = new GestureDetector(requireContext(), new SwipeGestureDetector());

        binding = FragmentFormSaleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        binding.expandable.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void expandLayout() {
        ViewGroup.LayoutParams params = binding.expandable.getLayoutParams();
        /*params.height = ViewGroup.LayoutParams.WRAP_CONTENT;*/
        params.height = 100;
        binding.expandable.setLayoutParams(params);
    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;

            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            // Swipe right
                        } else {
                            // Swipe left
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            // Swipe down
                        } else {
                            // Swipe up
                            expandLayout();
                            result = true;
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return result;
        }
    }
}