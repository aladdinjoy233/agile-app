package com.example.agile.ui.primary;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.agile.MainActivity;
import com.example.agile.R;
import com.example.agile.models.Usuario;

public class PrimaryViewModel extends AndroidViewModel {

    private Context context;

    public PrimaryViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
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
}
