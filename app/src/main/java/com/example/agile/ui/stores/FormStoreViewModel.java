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

    public FormStoreViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Boolean> getEditMode() {
        return isEditMode;
    }

}
