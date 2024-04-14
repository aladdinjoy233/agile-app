package com.example.agile.ui.primary.ui.sales;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FormSaleViewModel extends ViewModel {
    private MutableLiveData<Boolean> isMenuExpanded = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsMenuExpanded() {
        return isMenuExpanded;
    }

    public void setIsMenuExpanded(boolean isMenuExpanded) {
        this.isMenuExpanded.setValue(isMenuExpanded);
    }

    public FormSaleViewModel() {
        isMenuExpanded.setValue(false);
    }
}