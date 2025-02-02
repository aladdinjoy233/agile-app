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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsViewModel extends AndroidViewModel {
    private Context context;
    MutableLiveData<ArrayList<Categoria>> categorias = new MutableLiveData<>();
    MutableLiveData<ArrayList<Producto>> productos = new MutableLiveData<>();
    MutableLiveData<ArrayList<Producto>> productosFiltrados = new MutableLiveData<>();

    String query = "";
    Set<Integer> categoriasSeleccionadas = new HashSet<>();

    public ProductsViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        obtenerCategorias();
        obtenerProductos();
    }

    public MutableLiveData<ArrayList<Categoria>> getCategorias() { return categorias; }

    public MutableLiveData<ArrayList<Producto>> getProductos() { return productos; }

    public MutableLiveData<ArrayList<Producto>> getProductosFiltrados() { return productosFiltrados; }


    public void setQuery(String query) {
        this.query = query;
    }

    public void setCategoriasSeleccionadas(Set<Integer> categoriasSeleccionadas) {
        this.categoriasSeleccionadas = categoriasSeleccionadas;
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

    public void obtenerProductos() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storeId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<List<Producto>> call = endpoint.obtenerProductos(token, storeId);

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Producto> productosList = (ArrayList<Producto>) response.body();
                    productos.setValue(productosList);
                    productosFiltrados.setValue(productosList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener los productos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void filtrarProductos() {
        if (productos.getValue() == null) return;

        ArrayList<Producto> filteredProducts = new ArrayList<>();
        for (Producto p : productos.getValue()) {
            boolean matchesSearch = p.getNombre().toLowerCase().contains(query.toLowerCase());
            boolean matchesCategory = categoriasSeleccionadas.isEmpty() || categoriasSeleccionadas.contains(p.getCategoriaId());
            if (matchesSearch && matchesCategory) {
                filteredProducts.add(p);
            }
        }

        productosFiltrados.setValue(filteredProducts);
    }
}