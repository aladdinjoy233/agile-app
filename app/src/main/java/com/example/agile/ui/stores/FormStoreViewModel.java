package com.example.agile.ui.stores;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FormStoreViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Boolean> isEditMode = new MutableLiveData<>();
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
            // Write logic to update the store in the DB
        } else {
            // Write logic to insert the store in the DB
        }
    }

}
