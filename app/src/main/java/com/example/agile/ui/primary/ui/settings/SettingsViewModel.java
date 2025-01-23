package com.example.agile.ui.primary.ui.settings;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agile.models.Tienda;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;
import com.example.agile.ui.primary.PrimaryActivity;
import com.example.agile.ui.primary.PrimaryViewModel;
import com.example.agile.ui.stores.StoreActivity;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsViewModel extends AndroidViewModel {

    private Context context;
    MutableLiveData<Boolean> isOwner = new MutableLiveData<>();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        obtenerIsOwner();
    }

    public MutableLiveData<Boolean> getIsOwner() {
        return isOwner;
    }

    public void obtenerIsOwner() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storedStoreId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<Boolean> call = endpoint.esDuenio(token, storedStoreId);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isOwner.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {}
        });
    }

    public void salirTienda() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int tiendaId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<Void> call = endpoint.salirTienda(token, tiendaId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Saliste de la tienda con éxito", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove("selected_store_id");
                    editor.apply();

                    Intent intent = new Intent(context, StoreActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Error al salir de la tienda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("ERROR", Objects.requireNonNull(t.getMessage()));
            }
        });
    }


}