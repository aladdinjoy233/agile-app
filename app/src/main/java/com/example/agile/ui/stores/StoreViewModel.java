package com.example.agile.ui.stores;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.agile.MainActivity;
import com.example.agile.R;
import com.example.agile.models.Tienda;
import com.example.agile.models.Usuario;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreViewModel extends AndroidViewModel {

    private Context context;
    MutableLiveData<Usuario> usuario = new MutableLiveData<>();
    MutableLiveData<ArrayList<Tienda>> tiendas;

    public StoreViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        tiendas = new MutableLiveData<>();
        obtenerTiendas();
    }

    public MutableLiveData<Usuario> getUsuario() {
        if (this.usuario.getValue() == null) {
            Usuario usuario = new Usuario("Usuario");
            this.usuario.setValue(usuario);
        }
        return usuario;
    }

    public MutableLiveData<ArrayList<Tienda>> getTiendas() { return tiendas; }

    public void init(Usuario usuario) {
        if (usuario != null) {
            this.usuario.setValue(usuario);
        } else {
            obtenerUsuario();
        }
    }

    public void obtenerUsuario() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<Usuario> call = endpoint.obtenerUsuario(token);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StoreViewModel.this.usuario.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {}
        });
    }

    public void obtenerTiendas() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<List<Tienda>> call = endpoint.obtenerTiendas(token);

        call.enqueue(new Callback<List<Tienda>>() {
            @Override
            public void onResponse(Call<List<Tienda>> call, Response<List<Tienda>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tiendas.setValue(new ArrayList<>(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Tienda>> call, Throwable t) {
                Toast.makeText(context, "Error al obtener tiendas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logout() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("token");
        editor.apply();

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void nuevaTienda() {
        Intent intent = new Intent(context, FormStoreActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("editMode", false);
        context.startActivity(intent);
    }

}
