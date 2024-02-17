package com.example.agile;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.agile.models.Usuario;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;
import com.example.agile.ui.stores.StoreActivity;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private Context context;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void login(String email, String password) {
//        Validar que no esté vacio
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Campo vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario user = new Usuario(email, password);
        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<String> call = endpoint.login(user);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token", "Bearer " + response.body());
                        editor.apply();

                        Intent intent = new Intent(context, StoreActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }

                } else {
                    if (response.code() == 400) {
                        Toast.makeText(context, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE", Objects.requireNonNull(response.message()));
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
//                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("RESPONSE", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}
