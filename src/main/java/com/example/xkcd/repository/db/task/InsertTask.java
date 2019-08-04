package com.example.xkcd.repository.db.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xkcd.repository.db.InfoDao;
import com.example.xkcd.repository.info.InfoEntity;

public class InsertTask extends AsyncTask<InfoEntity, Void, Void> {

    private static final String TAG = "InsertTask";

    private InfoDao mDao;

    public InsertTask(InfoDao dao) {
        Log.d(TAG, "InsertTask: (dao)");
        mDao = dao;
    }

    @Override
    protected Void doInBackground(InfoEntity... infos) {
        Log.d(TAG, "doInBackground: " + infos[0]);
        mDao.insert(infos[0]);
        return null;
    }
}
