package com.example.agile.ui.primary.ui.sales;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.agile.R;
import com.example.agile.models.Venta;
import com.example.agile.models.VentaItem;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesViewModel extends AndroidViewModel {

    private Context context;

//    Ventas
    MutableLiveData<List<Venta>> ventas = new MutableLiveData<>();

    public SalesViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        obtenerVentas();
    }

    public LiveData<List<Venta>> getVentas() { return ventas; }

    public void obtenerVentas() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storeId = sp.getInt("selected_store_id", -1);

        if  (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<List<Venta>> call = endpoint.obtenerVentas(token, storeId);

        call.enqueue(new Callback<List<Venta>>() {
            @Override
            public void onResponse(@NonNull Call<List<Venta>> call, @NonNull Response<List<Venta>> response) {
                if (response.isSuccessful()) {
                    ventas.setValue(response.body());

                    List<Venta> ventaList = response.body();
                    for (Venta venta : ventaList) {
                        for (VentaItem item : venta.getVentaItems()) {
                            Log.d("Ventas", item.getNombre());
                        }
                        Log.d("Ventas", "---------------------");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Venta>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener las ventas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}