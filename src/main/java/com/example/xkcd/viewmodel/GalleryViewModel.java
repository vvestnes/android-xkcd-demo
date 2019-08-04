package com.example.xkcd.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xkcd.repository.Repository;

import java.util.List;

public class GalleryViewModel extends AndroidViewModel {

    private static final String TAG = "GalleryViewModel";

    private int mSource;
    private int mPageSelected;
    private Repository mRepo;

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        mRepo = Repository.getInstance(application);
    }

    public void search(String query) {
        Log.d(TAG, "search: " + query);
    }

    public LiveData<List<Integer>> getCurrentData() {
        return mRepo.getCurrentList();
    }

    public LiveData<List<Integer>> getSearchData(String query) {
        return mRepo.getSearchData(query);
    }

    public LiveData<List<Integer>> getBrowseData() {
        return mRepo.getBrowseData();
    }

    public LiveData<List<Integer>> getFavoritesData() {
        return mRepo.getFavoritesData();
    }

    public void setPageSelected(int pos) {
        mPageSelected = pos;
    }

    public int getPageSelected() {
        return mPageSelected;
    }

    public int getSource() {
        return mSource;
    }

    public void setSource(int source) {
        mSource = source;
    }
}

