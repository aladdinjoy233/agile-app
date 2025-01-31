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
import com.example.agile.models.Tienda;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsViewModel extends AndroidViewModel {
    private Context context;
    MutableLiveData<ArrayList<Categoria>> categorias = new MutableLiveData<>();

    public ProductsViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        obtenerCategorias();
    }

    public MutableLiveData<ArrayList<Categoria>> getCategorias() { return categorias; }

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
}