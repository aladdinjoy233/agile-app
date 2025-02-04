package com.example.agile.ui.primary.ui.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProductSharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> refreshTrigger = new MutableLiveData<>(false);

    public LiveData<Boolean> getRefreshTrigger() {
        return refreshTrigger;
    }

    public void triggerRefresh() {
        refreshTrigger.setValue(true);
    }

    public void resetRefresh() {
        refreshTrigger.setValue(false);
    }
}
