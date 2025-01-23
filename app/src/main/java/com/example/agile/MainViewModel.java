package com.example.agile;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.agile.models.Tienda;
import com.example.agile.models.Usuario;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;
import com.example.agile.ui.primary.PrimaryActivity;
import com.example.agile.ui.stores.StoreActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Boolean> session = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Boolean> getSession() {
        return session;
    }

    public void login() {
//        Go to login activity
        Intent intent = new Intent(context, LoginActivity.class);
//        "Intent.FLAG_ACTIVITY_CLEAR_TASK" es para limpiar la pila de activities anteriores
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void signup() {
//        Go to signup activity
        Intent intent = new Intent(context, SignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void verifySession() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (token.isEmpty()) {
            session.setValue(false);
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<Usuario> call = endpoint.obtenerUsuario(token);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    session.setValue(true);

                    Usuario usuario = response.body();
                    checkStoredStore(usuario, token);

//                    Intent intent = new Intent(context, StoreActivity.class);
//                    intent.putExtra("usuario", usuario);
//                    Log.d("USUARIO", usuario.toString());
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
                } else {
                    session.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                session.setValue(false);
            }
        });
    }

    private void checkStoredStore(Usuario usuario, String token) {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        int storedStoreId = sp.getInt("selected_store_id", -1);

        if (storedStoreId == -1) {
//            No tiene tienda guardado
            goToStoreActivity(usuario);
            return;
        }

//        Verificamos que exista la tienda
        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<Tienda> call = endpoint.obtenerTienda(token, storedStoreId);

        call.enqueue(new Callback<Tienda>() {
            @Override
            public void onResponse(@NonNull Call<Tienda> call, @NonNull Response<Tienda> response) {
                if (response.isSuccessful() && response.body() != null) {
                    goToPrimaryActivity(usuario);
                } else {
                    sp.edit().remove("selected_store_id").apply();
                    goToStoreActivity(usuario);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Tienda> call, @NonNull Throwable t) {
                goToStoreActivity(usuario);
            }
        });
    }

    private void goToStoreActivity(Usuario usuario) {
        Intent intent = new Intent(context, StoreActivity.class);
        intent.putExtra("usuario", usuario);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void goToPrimaryActivity(Usuario usuario) {
        Intent intent = new Intent(context, PrimaryActivity.class);
        intent.putExtra("usuario", usuario);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
