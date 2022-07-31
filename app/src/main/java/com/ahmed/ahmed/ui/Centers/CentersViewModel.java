package com.ahmed.ahmed.ui.Centers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CentersViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CentersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Centers fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}