package com.example.agile.ui.stores;

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
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormStoreViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Boolean> isEditMode = new MutableLiveData<>();
    private MutableLiveData<Boolean> operationCompleted = new MutableLiveData<>();
    // private MutableLiveData<Tienda> tienda = new MutableLiveData<>();

    public FormStoreViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Boolean> getEditMode() {
        if (isEditMode.getValue() == null) {
            isEditMode.setValue(false);
        }
        return isEditMode;
    }

    public LiveData<Boolean> getOperationCompleted() {
        return operationCompleted;
    }

    /*
    public LiveData<Tienda> getTienda() {
        return tienda;
    }
    */

    public void init(Boolean editMode) {
        isEditMode.setValue(editMode);

        // Write logic to get the store from the DB and save it in tienda
    }

    public void guardarTienda(String nombre, String email, String phone) {
        if (isEditMode.getValue()) {
            // Si es para editar
        } else {
            // Si es para crear
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

            Tienda tienda = new Tienda(nombre, email, phone);
            EndpointAgile endpoint = ApiClient.getEndpoint();
            Call<Void> call = endpoint.crearTienda(token, tienda);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                    if (response.isSuccessful()) {
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
//                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE", Objects.requireNonNull(t.getMessage()));
                }
            });
        }
    }

}
