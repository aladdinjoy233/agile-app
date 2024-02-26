package com.example.agile.ui.primary;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.agile.R;
import com.example.agile.models.Usuario;

public class PrimaryViewModel extends AndroidViewModel {

    private Context context;

    public PrimaryViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
}
