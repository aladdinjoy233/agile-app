package com.example.agile.ui.primary;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.agile.MainActivity;
import com.example.agile.R;
import com.example.agile.models.Tienda;
import com.example.agile.models.Usuario;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;
import com.example.agile.ui.stores.StoreActivity;
import com.example.agile.ui.stores.StoreViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrimaryViewModel extends AndroidViewModel {

    private Context context;
    MutableLiveData<Tienda> tienda = new MutableLiveData<>();

    public PrimaryViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        obtenerTienda();
    }

    public MutableLiveData<Tienda> getTienda() {
        return tienda;
    }

    public void obtenerTienda() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storedStoreId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<Tienda> call = endpoint.obtenerTienda(token, storedStoreId);

        call.enqueue(new Callback<Tienda>() {
            @Override
            public void onResponse(@NonNull Call<Tienda> call, @NonNull Response<Tienda> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PrimaryViewModel.this.tienda.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Tienda> call, @NonNull Throwable t) {}
        });
    }

    public void logout() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("token");
        editor.remove("selected_store_id");
        editor.apply();

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void cambiarTienda() {
        Intent intent = new Intent(context, StoreActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
