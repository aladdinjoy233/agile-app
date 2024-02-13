package com.example.agile;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.agile.models.Usuario;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupViewModel extends AndroidViewModel {

    private Context context;

    public SignupViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void signup(String name, String email, String password) {
//        Validar que no esté vacio
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Campo vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario user = new Usuario(name, email, password);
        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<String> call = endpoint.signup(user);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        SharedPreferences sp = context.getSharedPreferences("agile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token", "Bearer " + response.body());
                        editor.commit();

//                        Intent intent = new Intent(context, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        context.startActivity(intent);

                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show();
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
