package com.example.agile.ui.primary.ui.sales;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SaleSharedViewModel extends ViewModel {
    MutableLiveData<Boolean> refreshTrigger = new MutableLiveData<>();

    public LiveData<Boolean> getRefreshTrigger() { return refreshTrigger; }
    public void triggerRefresh() { refreshTrigger.setValue(true); }
    public void resetRefresh() { refreshTrigger.setValue(false); }

}
