package com.example.agile.ui.primary.ui.settings;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agile.models.Tienda;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormStoreViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Boolean> operationCompleted = new MutableLiveData<>();
    private MutableLiveData<Tienda> tienda = new MutableLiveData<>();
    private int tiendaId = -1;

    public FormStoreViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        obtenerTienda();
    }

    public LiveData<Boolean> getOperationCompleted() {
        return operationCompleted;
    }
    public MutableLiveData<Tienda> getTienda() {
        return tienda;
    }

    private void obtenerTienda() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        tiendaId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<Tienda> call = endpoint.obtenerTienda(token, tiendaId);

        call.enqueue(new Callback<Tienda>() {
            @Override
            public void onResponse(@NonNull Call<Tienda> call, @NonNull Response<Tienda> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tienda.setValue(response.body());
                } else {
                    Toast.makeText(context, "Error al obtener la tienda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Tienda> call, @NonNull Throwable t) {
                Log.d("RESPONSE", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public void guardarTienda(String nombre, String email, String phone) {

        // Validar que no esté vacío el nombre
        if (nombre.isEmpty()) {
            Toast.makeText(context, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (token.isEmpty()) {
            return;
        }

        Tienda tiendaData = new Tienda(nombre, email, phone);
        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<Void> call = endpoint.editarTienda(token, tiendaId, tiendaData);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Tienda editada con exito", Toast.LENGTH_SHORT).show();
                    operationCompleted.setValue(true);
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(context, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE", Objects.requireNonNull(response.message()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("RESPONSE", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}