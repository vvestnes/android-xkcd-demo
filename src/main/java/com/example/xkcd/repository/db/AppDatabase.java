package com.example.xkcd.repository.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.xkcd.repository.info.InfoEntity;
//import com.example.xkcd.repository.entity.Favorite;


@Database(entities = {InfoEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract InfoDao getInfoDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    AppDatabase.class, "xkcd_database")
                    .fallbackToDestructiveMigration()
                    //.addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(instance).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private InfoDao mDao;

        private PopulateDbAsync(AppDatabase db) {
            mDao = db.getInfoDao();
        }

        /*
         * Add some testdata
         */
        @Override
        protected Void doInBackground(final Void... params) {
            for (int i = 1; i <= 2; i++) {
                mDao.insert(new InfoEntity(
                        Integer.toString(i),
                        1000 + i,
                        "Link " + i,
                        Integer.toString(2005 + i),
                        "News " + i,
                        "Safe Title " + i,
                        "Transcript " + i,
                        "Alt " + i,
                        "http://img.com/" + i + ".jpg",
                        "Title " + i,
                        Integer.toString(5 + i),
                        false
                ));
            }
            return null;
        }
    }
}
