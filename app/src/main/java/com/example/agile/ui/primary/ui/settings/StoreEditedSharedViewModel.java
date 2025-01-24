package com.example.agile.ui.primary.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StoreEditedSharedViewModel extends ViewModel {
    private MutableLiveData<Boolean> edicionCompletada = new MutableLiveData<>(false);

    public LiveData<Boolean> getEdicionCompletada() {
        return edicionCompletada;
    }

    public void setEdicionCompletada(Boolean edicionCompletada) {
        this.edicionCompletada.setValue(edicionCompletada);
    }

//    Reset
    public void reset() {
        edicionCompletada.setValue(false);
    }
}
