package com.example.xkcd.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.xkcd.App;
import com.example.xkcd.R;
import com.example.xkcd.activity.MainActivity;
import com.example.xkcd.repository.api.XkcdInfoApi;
import com.example.xkcd.repository.info.Info;
import com.example.xkcd.util.PrefsUtil;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DailyReminderService extends JobService {

    private static final String TAG = "DailyReminderService";

    private static final int JOB_ID = 1;

    public static void schedule(Context context, long intervalMillis) {
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName =
                new ComponentName(context, DailyReminderService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPeriodic(intervalMillis);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        startBackgroundWork(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void startBackgroundWork(final JobParameters params) {
        Log.d(TAG, "doBackgroundWork: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                doBackgroundWork();
                jobFinished(params, false);
            }
        }).start();
    }

    private void doBackgroundWork() {
        Log.d(TAG, "doBackgroundWork: ");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        XkcdInfoApi infoApi = retrofit.create(XkcdInfoApi.class);

        try {
            Response<Info> response = infoApi.getCurrentInfo().execute();
            if (response.isSuccessful() && response.body() != null) {
                Info info = response.body();
                int currentNum = PrefsUtil.getCurrent(getApplicationContext());
                if (currentNum != info.getNum()) {
                    Log.d(TAG, "run: currentNum NEW! " + info.getNum() + " (old was " + currentNum + ")");
                    PrefsUtil.setCurrent(getApplicationContext(), info.getNum());
                    createNotificationAlert(info);
                } else {
                    Log.d(TAG, "run: currentNum unchanged, no reason to notify user");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNotificationAlert(Info info) {
        Log.d(TAG, "createNotificationAlert: ");
        String title = getString(R.string.alert_title).replace("[num]", Integer.toString(info.getNum()));
        String message = getString(R.string.alert_message).replace("[title]", info.getTitle());

        Intent activityIntent = new Intent(this, MainActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplication());
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.GREEN)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                //.setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, getString(R.string.action_open), contentIntent)
                .build();
        notificationManagerCompat.notify(1, notification);
    }
}