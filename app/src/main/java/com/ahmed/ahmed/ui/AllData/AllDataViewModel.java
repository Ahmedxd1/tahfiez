package com.ahmed.ahmed.ui.AllData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllDataViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AllDataViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}