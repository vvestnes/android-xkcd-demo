package com.example.xkcd.repository;

import androidx.lifecycle.MutableLiveData;

public class RemoteLiveData<T1> extends MutableLiveData<Boolean>{

    private boolean mIsLoading;
    private String mError;
    private T1 mLiveData;

    public RemoteLiveData() {
        super();
        setValue(true);
    }

    public void setLiveData(T1 liveData) {
        mLiveData = liveData;
    }

    public T1 getLiveData() {
        return mLiveData;
    }

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean isLoading) {
        mIsLoading = isLoading;
    }
}

