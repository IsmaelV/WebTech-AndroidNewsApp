package com.example.hw_9_webtech.ui.headlines;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HeadlinesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HeadlinesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the headlines fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}