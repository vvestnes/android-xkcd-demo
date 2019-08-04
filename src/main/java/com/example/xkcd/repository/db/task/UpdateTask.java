package com.example.xkcd.repository.db.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xkcd.repository.db.InfoDao;
import com.example.xkcd.repository.info.InfoEntity;

public class UpdateTask extends AsyncTask<InfoEntity, Void, Void> {

    private static final String TAG = "UpdateTask";
    
    private InfoDao mDao;

    public UpdateTask(InfoDao dao) {
        Log.d(TAG, "UpdateTask: (dao)");
        mDao = dao;
    }

    @Override
    protected Void doInBackground(InfoEntity... infos) {
        Log.d(TAG, "doInBackground: " + infos[0]);
        mDao.update(infos[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d(TAG, "onPostExecute: ");
    }
}
