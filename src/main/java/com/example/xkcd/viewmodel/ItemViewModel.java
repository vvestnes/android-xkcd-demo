package com.example.xkcd.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xkcd.repository.info.InfoEntity;
import com.example.xkcd.repository.info.Info;
import com.example.xkcd.repository.Repository;

public class ItemViewModel extends AndroidViewModel {

    private static final String TAG = "ItemViewModel";
    
    private Repository mRepo;

    private LiveData<Info> mInfo;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "ItemViewModel: ");
        mRepo = Repository.getInstance(application);
    }

    public LiveData<InfoEntity> getInfo(int num) {
        return mRepo.getInfo(num);
    }

    public LiveData<InfoEntity> getInfo2(int num) {
        return mRepo.getLiveDataLoader(num);
    }

    public void updateInfo(InfoEntity info) {
        mRepo.updateInfo(info);
    }
}