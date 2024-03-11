package com.example.agile.ui.primary.ui.sales;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.agile.R;

public class SalesViewModel extends AndroidViewModel {

    private Context context;

    public SalesViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
}