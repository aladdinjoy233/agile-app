package com.example.agile.ui.primary.ui.sales;

import static androidx.core.content.ContextCompat.getSystemService;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.OptIn;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Size;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.agile.R;
import com.example.agile.databinding.FragmentFormSaleBinding;
import com.example.agile.ui.primary.ui.products.FilterAdapter;
import com.example.agile.ui.primary.ui.products.FormProductFragment;
import com.example.agile.ui.primary.ui.products.ProductAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FormSaleFragment extends Fragment implements FilterAdapter.OnCategorySelectionListener, VentaItemAdapter.ItemListener {

    private FragmentFormSaleBinding binding;

    private BottomSheetBehavior<LinearLayout> bsbMenu;
    private BottomSheetBehavior<LinearLayout> bsbList;
    private FormSaleViewModel vm;
    private SaleSharedViewModel sharedViewModel;
    private EditText etSearch;

//    Barcode scan
    private androidx.camera.view.PreviewView previewView;
    private FrameLayout scannerContainer;
    private ExecutorService cameraExecutor;
    private BarcodeScanner barcodeScanner;
    private ProcessCameraProvider cameraProvider;
    private MediaPlayer beepPlayer;
    private boolean isScanning = false;

//    Permission launcher
    private ActivityResultLauncher<String> cameraPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    startScanner();
                } else {
                    Toast.makeText(requireContext(), "Acceso a la camara es necesario para escanear codigos de barra.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Initialize barcode scanner
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build();

        barcodeScanner = BarcodeScanning.getClient(options);

//        Initialize camera executor
        cameraExecutor = Executors.newSingleThreadExecutor();

//        Initialize beep player
        beepPlayer = MediaPlayer.create(requireContext(), R.raw.beep_sound);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(FormSaleViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SaleSharedViewModel.class);

        binding = FragmentFormSaleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etSearch = binding.etSearch;
        previewView = binding.previewView;
        scannerContainer = binding.scannerContainer;

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

//        Textwatcher para la busqueda
        binding.etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.btClear.setVisibility(s.toString().trim().isEmpty() ? View.GONE : View.VISIBLE);
                vm.setQuery(s.toString());
                vm.filtrarProductos();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btClear.setOnClickListener(v -> {
            binding.etSearch.setText("");
            binding.etSearch.clearFocus();
            hideKeyboard();
        });

//        Cerrar teclado cuando tocan la decla de busqueda
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard();
                return true;
            }
            return false;
        });

//        Scanner
        binding.btCodigo.setOnClickListener(v -> {
            if (isScanning) {
                stopScanner();
            } else {
                verificarPermisoCamaraYEscanear();
            }
        });

//        Guardar venta
        binding.btFinish.setOnClickListener(v -> {
            dialogGuardarVenta();
        });

        vm.getOperationCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (Boolean.TRUE.equals(completed)) {
                sharedViewModel.triggerRefresh();
                getParentFragmentManager().popBackStack();
            }
        });

        return root;
    }

    private void dialogGuardarVenta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Guardar venta");
        builder.setMessage("¿Desea guardar la venta?");
        builder.setPositiveButton("Si", (dialog, which) -> {
            vm.guardarVenta();
            dialog.dismiss();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private void verificarPermisoCamaraYEscanear() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanner();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void startScanner() {
//        Show scaner container
        scannerContainer.setVisibility(View.VISIBLE);
        isScanning = true;

//        Start camera
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

//                Setup preview use case
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

//                Setup image analysis use case
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, new FormSaleFragment.BarcodeAnalyzer());

//                Select back camera
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

//                Bind use cases to camera
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(requireActivity(), cameraSelector, preview, imageAnalysis);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Failed to start camera", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void stopScanner() {
//        Hide scanner container
        scannerContainer.setVisibility(View.GONE);
        isScanning = false;

//        Unbind camera
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    private class BarcodeAnalyzer implements ImageAnalysis.Analyzer {

        @OptIn(markerClass = ExperimentalGetImage.class)
        @Override
        public void analyze(@NonNull ImageProxy imageProxy) {
//            @androidx.camera.core.ExperimentalGetImage
            InputImage inputImage = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());

            barcodeScanner.process(inputImage)
                    .addOnSuccessListener(barcodes -> {
                        if (!barcodes.isEmpty()) {
                            Barcode barcode = barcodes.get(0);
                            String barcodeValue = barcode.getRawValue();

                            if (barcodeValue != null) {
                                requireActivity().runOnUiThread(() -> {
//                                Play beep sound
                                    if (beepPlayer != null) {
                                        beepPlayer.start();
                                    }

                                    vm.codigoEscaneado(barcodeValue);

                                    stopScanner();
                                });
                            }
                        }
                        imageProxy.close();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Error al leer el codigo de barras", Toast.LENGTH_SHORT).show();
                        imageProxy.close();
                    });
        }

        @Nullable
        @Override
        public Size getDefaultTargetResolution() {
            return ImageAnalysis.Analyzer.super.getDefaultTargetResolution();
        }

        @Override
        public int getTargetCoordinateSystem() {
            return ImageAnalysis.Analyzer.super.getTargetCoordinateSystem();
        }

        @Override
        public void updateTransform(@Nullable Matrix matrix) {
            ImageAnalysis.Analyzer.super.updateTransform(matrix);
        }
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
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
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