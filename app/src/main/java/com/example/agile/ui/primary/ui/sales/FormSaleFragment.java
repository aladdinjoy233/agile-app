package com.example.agile.ui.primary.ui.sales;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.agile.R;
import com.example.agile.databinding.FragmentFormSaleBinding;
import com.example.agile.ui.primary.ui.products.FilterAdapter;
import com.example.agile.ui.primary.ui.products.ProductAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Set;

public class FormSaleFragment extends Fragment implements FilterAdapter.OnCategorySelectionListener, VentaItemAdapter.ItemListener {

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

//        Precio total
        vm.getPrecioTotal().observe(getViewLifecycleOwner(), precioTotal -> binding.tvTotal.setText("Total: " + precioTotal));

//        Bottom sheet menu
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

//        Loading
        vm.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading)
                binding.pbLoading.setVisibility(View.VISIBLE);
            else
                binding.pbLoading.setVisibility(View.GONE);
        });

//        Categorías
        vm.getCategorias().observe(getViewLifecycleOwner(), categorias -> {
//            Si no hay categorías en la lista, ocultar el filtro
            if (categorias.isEmpty()) {
                binding.rvFilters.setVisibility(View.GONE);
                return;
            }

            binding.rvFilters.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
            FilterAdapter adapter = new FilterAdapter(categorias, inflater, this);
            binding.rvFilters.setAdapter(adapter);
        });

//        Productos
        vm.getProductosFiltrados().observe(getViewLifecycleOwner(), productos -> {
            binding.rvProductos.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
            SaleProductAdapter adapter = new SaleProductAdapter(productos, inflater, productId -> {
                vm.agregarProductoALaVenta(productId);
            });
            binding.rvProductos.setAdapter(adapter);
        });

        vm.getProductosSeleccionados().observe(getViewLifecycleOwner(), productos -> {
            binding.rvVentas.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
            VentaItemAdapter adapter = new VentaItemAdapter(productos, inflater, this);
            binding.rvVentas.setAdapter(adapter);
        });

//        TODO: Agregar busqueda y escaneo de código de barras

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

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onCategorySelectionChanged(Set<Integer> selectedCategories) {
        vm.setCategoriasSeleccionadas(selectedCategories);
        vm.filtrarProductos();
    }

//    Venta item listener
    @Override
    public void onButtonSumarClick(int productoId) { vm.sumarCantidad(productoId); }

    @Override
    public void onButtonRestarClick(int productoId) { vm.restarCantidad(productoId); }
}