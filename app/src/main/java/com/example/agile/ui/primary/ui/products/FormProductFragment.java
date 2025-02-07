package com.example.agile.ui.primary.ui.products;

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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.agile.R;
import com.example.agile.databinding.FragmentFormProductBinding;
import com.example.agile.databinding.FragmentListUserBinding;
import com.example.agile.models.Categoria;
import com.example.agile.ui.primary.ui.settings.ListUserViewModel;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FormProductFragment extends Fragment {

    private FragmentFormProductBinding binding;
    private FormProductViewModel vm;
    private ProductSharedViewModel sharedViewModel;
    private ArrayAdapter<String> categoriaAdapter;
    private List<Categoria> categoriaList = new ArrayList<>();

    private EditText etCodigo;
    private androidx.camera.view.PreviewView previewView;
    private FrameLayout scannerContainer;

    private ExecutorService cameraExecutor;
    private BarcodeScanner barcodeScanner;
    private ProcessCameraProvider cameraProvider;
    private MediaPlayer beepPlayer;
    private boolean isScanning = false;


//    Permission launcher for camera
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

//        Prepare beep sound
        beepPlayer = MediaPlayer.create(requireContext(), R.raw.beep_sound);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(FormProductViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(ProductSharedViewModel.class);

        binding = FragmentFormProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etCodigo = binding.etCodigo;
        previewView = binding.previewView;
        scannerContainer = binding.scannerContainer;

//        Setup Spinner
        setupCategoriasSpinner();

        Bundle args = getArguments();
        boolean editMode;
//        Init ViewModel
        if (args != null) {
            editMode = args.getBoolean("editMode");
            vm.setProductId(args.getInt("productId"));
        } else {
            editMode = false;
        }

        vm.getEditMode().observe(getViewLifecycleOwner(), isEdit -> {
            if (isEdit) {
                binding.tvTitle.setText("Editar producto");
                binding.btFinish.setText("Guardar");
                binding.btDelete.setVisibility(View.VISIBLE);
            } else {
                binding.tvTitle.setText("Nuevo producto");
                binding.btFinish.setText("Crear");
            }
        });

        vm.getProducto().observe(getViewLifecycleOwner(), producto -> {
            binding.etCodigo.setText(producto.getCodigo());
            binding.etNombre.setText(producto.getNombre());
            binding.etPrecio.setText(String.valueOf(producto.getPrecio()));
            binding.etStock.setText(String.valueOf(producto.getStock()));

//            Setear categoria seleccionada
            if (producto.getCategoriaId() != 0) {
                int posicion = posicionCategoria(producto.getCategoriaId());
                if (posicion != -1) {
                    binding.spCategoria.setSelection(posicion);
                }
            }
        });

        vm.getCategorias().observe(getViewLifecycleOwner(), categorias -> {
            updateCategoriasSpinner(categorias);
        });

        vm.init(editMode);

        binding.btBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        vm.getOperationCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (Boolean.TRUE.equals(completed)) {
                sharedViewModel.triggerRefresh();
                getParentFragmentManager().popBackStack();
            }
        });

        binding.btFinish.setOnClickListener(v -> {
            float precio = (float) parseEditTextToDouble(binding.etPrecio);
            int stock = parseEditTextToInt(binding.etStock);
            vm.guardarProducto(binding.etCodigo.getText().toString(), binding.etNombre.getText().toString(), precio, stock);
        });

        binding.btDelete.setOnClickListener(v -> {
            dialogEliminarProducto();
        });

        binding.btCodigo.setOnClickListener(v -> {
            if (isScanning) {
                stopScanner();
            } else {
                verificarPermisoCamaraYEscanear();
            }
        } );

        return root;
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

                imageAnalysis.setAnalyzer(cameraExecutor, new BarcodeAnalyzer());

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

                                etCodigo.setText(barcodeValue);

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

    private int parseEditTextToInt(EditText editText) {
        String text = editText.getText().toString().trim();

        if (text.isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            editText.setError("Debe ser un valor numérico");
            return 0;
        }
    }

    private double parseEditTextToDouble(EditText editText) {
        String text = editText.getText().toString().trim();

        if (text.isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            editText.setError("Debe ser un valor numérico");
            return 0.0;
        }
    }

    private void setupCategoriasSpinner() {
//        Crear adapter con "Select Categoria"
        categoriaAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>());
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spCategoria.setAdapter(categoriaAdapter);

//        Setup listener para la seleccion
        binding.spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Categoria selectedCategoria = categoriaList.get(position - 1);
                    vm.setSelectedCategoria(selectedCategoria);
                } else {
                    vm.setSelectedCategoria(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm.setSelectedCategoria(null);
            }
        });
    }

    private void updateCategoriasSpinner(List<Categoria> categorias) {
        categoriaList = categorias;

//        Preparamos los items del spinner
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Seleccionar categoria");
        for (Categoria categoria : categorias) {
            spinnerItems.add(categoria.getNombre());
        }

//        Actualizamos el adapter
        categoriaAdapter.clear();
        categoriaAdapter.addAll(spinnerItems);
        categoriaAdapter.notifyDataSetChanged();
    }

    private int posicionCategoria(int categoriaId) {
        if (categoriaId == 0) return -1;

        for (int i = 0; i < categoriaList.size(); i++) {
            if (categoriaList.get(i).getCategoriaId() == categoriaId) return i + 1;
        }
        return -1;
    }

    private void dialogEliminarProducto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar eliminación")
                .setMessage("¿Desea eliminar este producto?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    vm.eliminarProducto();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert);

        AlertDialog dialog = builder.create();
        dialog.show();

//        Setear colores de los botónes (rojo para eliminar)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        cameraExecutor.shutdown();
        if (barcodeScanner != null) {
            barcodeScanner.close();
        }

        if (beepPlayer != null) {
            beepPlayer.release();
        }
    }

}