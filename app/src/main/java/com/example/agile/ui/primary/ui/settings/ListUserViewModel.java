package com.example.agile.ui.primary.ui.settings;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agile.models.Usuario;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUserViewModel extends AndroidViewModel {

    private Context context;
    MutableLiveData<ArrayList<Usuario>> usuarios;

    public ListUserViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        usuarios = new MutableLiveData<>();
        obtenerUsuarios();
    }

    public MutableLiveData<ArrayList<Usuario>> getUsuarios() {
        return usuarios;
    }

    public void obtenerUsuarios() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storedStoreId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<List<Usuario>> call = endpoint.obtenerUsuarios(token, storedStoreId);

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Usuario>> call, @NonNull Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    usuarios.setValue(new ArrayList<>(response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Usuario>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void invitarUsuario(String email) {

        if (email.isEmpty() || !esEmailValido(email)) {
            Toast.makeText(context, "Email inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storedStoreId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        Usuario usuarioEmail = new Usuario();
        usuarioEmail.setEmail(email);

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<Void> call = endpoint.invitarUsuario(token, storedStoreId, usuarioEmail);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Usuario invitado", Toast.LENGTH_SHORT).show();
                    obtenerUsuarios();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al invitar usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean esEmailValido(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void eliminarUsuario(Usuario usuario) {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storedStoreId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();

        if (usuario.getEsInvitado()) { // Si es una invitación, eliminamos la invitación

            Usuario invitacion = new Usuario();
            invitacion.setEmail(usuario.getEmail());
            Call<Void> call = endpoint.eliminarInvitacion(token, storedStoreId, invitacion);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                        obtenerUsuarios();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
                }
            });

        } else { // Si es un usuario, eliminamos el usuario de la lista de usuarios

            Call<Void> call = endpoint.eliminarUsuario(token, storedStoreId, usuario.getId());

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                        obtenerUsuarios();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}