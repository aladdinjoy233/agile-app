package com.example.agile;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class MainViewModel extends AndroidViewModel {
    private Context context;
    public MainViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
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
}
