package com.example.xkcd.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xkcd.repository.api.XkcdInfoApi;
import com.example.xkcd.repository.api.XkcdSearchApi;
import com.example.xkcd.repository.db.AppDatabase;
import com.example.xkcd.repository.db.InfoDao;
import com.example.xkcd.repository.db.task.InsertTask;
import com.example.xkcd.repository.db.task.UpdateTask;
import com.example.xkcd.repository.info.Info;
import com.example.xkcd.repository.info.InfoEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Todo: Implement better error handling.
 */
public class Repository {

    private static final String TAG = "Repository";

    private static Repository instance;

    private InfoDao mInfoDao;

    private XkcdInfoApi infoApi;
    private XkcdSearchApi searchApi;

    private Repository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        mInfoDao = db.getInfoDao();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        infoApi = retrofit.create(XkcdInfoApi.class);

        Retrofit retrofitSearch = new Retrofit.Builder()
                .baseUrl("https://relevantxkcd.appspot.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        searchApi = retrofitSearch.create(XkcdSearchApi.class);
    }

    public static Repository getInstance(Context context) {
        if (instance == null) {
            instance = new Repository(context);
        }
        return instance;
    }

    /**
     * Returns Info object containing all available info on a XKCD comic.
     *
     * @param num The uniqe id of the xkcd
     * @return Info object
     */
    public LiveData<InfoEntity> getInfo(int num) {
        Log.d(TAG, "getLiveData: " + num);

        final LiveData<InfoEntity> data = mInfoDao.getByNum(num);

        Call<Info> call = infoApi.getInfo(num);
        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(@NotNull Call<Info> call, @NotNull Response<Info> response) {
                if (data.getValue() != null || !response.isSuccessful() || response.body() == null) {
                    return;
                }
                InfoEntity entity = new InfoEntity(response.body());
                new InsertTask(mInfoDao).execute(entity);
            }

            @Override
            public void onFailure(@NotNull Call<Info> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: info could not be loaded from remote server: " + t.getMessage());
            }
        });
        return data;
    }

    /**
     * Returns list with one element, the id/num of latest xkcd.
     *
     * @return List with one single element, the id/num of latest/current xkcd.
     */
    public LiveData<List<Integer>> getCurrentList() {
        Log.d(TAG, "getCurrentList: ");

        final MutableLiveData<List<Integer>> data = new MutableLiveData<>();

        Call<Info> call = infoApi.getCurrentInfo();
        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(@NotNull Call<Info> call, @NotNull Response<Info> response) {
                Log.d(TAG, "onResponse: " + response);
                List<Integer> list = new ArrayList<>();
                if (response.isSuccessful() && response.body() != null) {
                    Info info = response.body();
                    list.add(response.body().getNum());
                    InfoEntity entity = new InfoEntity(info);
                    new InsertTask(mInfoDao).execute(entity);
                }
                data.postValue(list);
            }

            @Override
            public void onFailure(@NotNull Call<Info> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<InfoEntity> getLiveDataLoader(int num) {
        Log.d(TAG, "getLiveDataLoader: " + num);
        //final RemoteLiveData<LiveData<InfoEntity>> loader = new RemoteLiveData<>();
        //loader.setValue(true); // Set status to "Is loading"...
        final LiveData<InfoEntity> data = mInfoDao.getByNum(num);
        //loader.setLiveData(data);

        Call<Info> call = infoApi.getInfo(num);
        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(@NotNull Call<Info> call, @NotNull Response<Info> response) {
                Log.d(TAG, "onResponse: " + response);
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: unSuccessful");
                    //loader.setError("Something went wrong when trying to load data, code: " + response.code());
                } else if (response.body() == null) {
                    Log.d(TAG, "onResponse: body == null");
                    //loader.setError("An error occured. The response body from server was empty.");
                } else if (data.getValue() != null) {
                    Log.d(TAG, "onResponse: data already present in liveData. Skipping insert.");
                } else {
                    InfoEntity entity = new InfoEntity(response.body());
                    Log.d(TAG, "onResponse: inserting entity:" + entity);
                    new InsertTask(mInfoDao).execute(entity);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Info> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: info could not be loaded from remote server: " + t.getMessage());
                //loader.setError("Error: " + t.getMessage());
                //loader.postValue(false);
            }
        });
        return data;
    }

    /**
     * Returns list of all xkcd ids available (from 1 up to current/latest xkcd id)
     *
     * @return List of xkcd ids
     */
    public LiveData<List<Integer>> getBrowseData() {
        Log.d(TAG, "getBrowseData: ");

        final MutableLiveData<List<Integer>> data = new MutableLiveData<>();

        Call<Info> call = infoApi.getCurrentInfo();
        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(@NotNull Call<Info> call, @NotNull Response<Info> response) {
                Log.d(TAG, "getBrowseData onResponse: " + response);
                List<Integer> list = new ArrayList<>();
                if (response.isSuccessful() && response.body() != null) {
                    Info info = response.body();
                    int current = info.getNum();
                    for (int i = current; i >= 1; i--) {
                        list.add(i);
                    }
                }
                data.postValue(list);
            }

            @Override
            public void onFailure(@NotNull Call<Info> call, @NotNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    /**
     * Returns list of all xkcd ids favorited (as LiveData)
     *
     * @return list of xkcd ids favorited.
     */
    public LiveData<List<Integer>> getFavoritesData() {
        Log.d(TAG, "getFavoritesData: ");
        return mInfoDao.getAllNums();
    }


    /**
     * Get list of xkcd matching search
     * <p>
     * Todo: use a Custom Converter on response.
     *
     * @param query String
     * @return List of xkcd ids matching the query
     */
    public LiveData<List<Integer>> getSearchData(String query) {
        Log.d(TAG, "getSearchData: " + query);

        final MutableLiveData<List<Integer>> data = new MutableLiveData<>();

        Integer num = null;
        try {
            num = Integer.parseInt(query);
        } catch (NumberFormatException e) {
            Log.d(TAG, "getSearchData: query is not a number...");
        }

        if (num != null) {
            Call<Info> call = infoApi.getInfo(num);
            call.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(@NotNull Call<Info> call, @NotNull Response<Info> response) {

                    List<Integer> list = new ArrayList<>();
                    if (response.isSuccessful() && response.body() != null) {
                        Info info = response.body();
                        list.add(info.getNum());
                    }
                    data.postValue(list);
                }

                @Override
                public void onFailure(@NotNull Call<Info> call, @NotNull Throwable t) {
                    data.postValue(null);
                }
            });
        } else {
            Call<String> call = searchApi.search(query);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {

                    List<Integer> list = new ArrayList<>();
                    if (response.isSuccessful() && response.body() != null) {
                        String body = response.body();
                        String[] lines = body.split("\n");
                        for (int i = 2; i < lines.length; i++) {
                            list.add(Integer.parseInt(lines[i].split(" ")[0]));
                        }
                    }
                    data.postValue(list);
                }

                @Override
                public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                    data.postValue(null);
                }
            });
        }
        return data;
    }

    public void updateInfo(InfoEntity info) {
        Log.d(TAG, "updateInfo: " + info);
        new UpdateTask(mInfoDao).execute(info);
    }


}

