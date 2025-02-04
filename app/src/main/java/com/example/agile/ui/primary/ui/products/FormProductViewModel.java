package com.example.agile.ui.primary.ui.products;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agile.models.Categoria;
import com.example.agile.models.Producto;
import com.example.agile.models.Tienda;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormProductViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Boolean> isEditMode = new MutableLiveData<>();
    private MutableLiveData<Boolean> operationCompleted = new MutableLiveData<>();
    private MutableLiveData<Producto> producto = new MutableLiveData<>();
    private int productId = -1;
    private MutableLiveData<List<Categoria>> categorias = new MutableLiveData<>();
    private int selectedCategoriaId = 0;

    public FormProductViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        obtenerCategorias();
    }

    public LiveData<Boolean> getEditMode() {
        if (isEditMode.getValue() == null) {
            isEditMode.setValue(false);
        }
        return isEditMode;
    }

    public LiveData<Boolean> getOperationCompleted() { return operationCompleted; }
    public LiveData<Producto> getProducto() { return producto; }
    public LiveData<List<Categoria>> getCategorias() { return categorias; }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    public void setSelectedCategoria(Categoria categoria) {
        selectedCategoriaId = categoria != null ? categoria.getCategoriaId() : 0;
    }

    public void init(Boolean editMode) {
        isEditMode.setValue(editMode);

//        Logica para obtener el producto de la base de datos
        if (editMode) {
            SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
            String token = sp.getString("token", "");
            int storeId = sp.getInt("selected_store_id", -1);

            if (token.isEmpty() || storeId == -1) {
                return;
            }

            EndpointAgile endpoint = ApiClient.getEndpoint();
            Call<Producto> call = endpoint.obtenerProducto(token, storeId, productId);

            call.enqueue(new Callback<Producto>() {
                @Override
                public void onResponse(@NonNull Call<Producto> call, @NonNull Response<Producto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        producto.setValue(response.body());
                    } else {
                        Toast.makeText(context, "Error al obtener el producto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Producto> call, @NonNull Throwable t) {
                    Log.d("RESPONSE", Objects.requireNonNull(t.getMessage()));
                }
            });
        }
    }

    public void obtenerCategorias() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storeId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<List<Categoria>> call = endpoint.obtenerCategorias(token, storeId);

        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(@NonNull Call<List<Categoria>> call, @NonNull Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categorias.setValue((ArrayList<Categoria>) response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Categoria>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener las categorías", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void guardarProducto(String codigo, String nombre, float precio, int stock) {

        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storeId = sp.getInt("selected_store_id", -1);

        Producto productoData = new Producto(storeId, codigo, nombre, precio, stock, selectedCategoriaId);
        EndpointAgile endpoint = ApiClient.getEndpoint();

        if (token.isEmpty()) {
            return;
        }

        if (Boolean.TRUE.equals(isEditMode.getValue())) { // Si es para editar

            Call<Void> call = endpoint.editarProducto(token, storeId, productId, productoData);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        operationCompleted.setValue(true);
                    } else {
                        Toast.makeText(context, "Error al editar el producto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Log.d("RESPONSE", Objects.requireNonNull(t.getMessage()));
                }
            });

        } else { // Si es para crear

            Call<Void> call = endpoint.crearProducto(token, storeId, productoData);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        operationCompleted.setValue(true);
                    } else {
                        Toast.makeText(context, "Error al crear el producto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Log.d("RESPONSE", Objects.requireNonNull(t.getMessage()));
                }
            });

        }
    }

    public void eliminarProducto() {

        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storeId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<Void> call = endpoint.eliminarProducto(token, storeId, productId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    operationCompleted.setValue(true);
                } else {
                    Toast.makeText(context, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("RESPONSE", Objects.requireNonNull(t.getMessage()));
            }
        });

    }
}