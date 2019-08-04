package com.example.xkcd.fragment;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.xkcd.R;
import com.example.xkcd.service.DailyReminderService;
import com.example.xkcd.util.PrefsUtil;

import static android.app.job.JobInfo.Builder;
import static android.app.job.JobInfo.NETWORK_TYPE_ANY;


public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private static final long ONE_DAY_INTERVAL = 24 * 60 * 60 * 1000L; // 1 Day
    private static final int JOB_ID = 123;

    private Context mCtx;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
        //Bundle args = new Bundle();
        //args.putString("num", num);
        //fragment.setArguments(args);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mCtx = getContext();

        Switch notificationSwitch = view.findViewById(R.id.notification_switch);
        notificationSwitch.setChecked(PrefsUtil.isNotificationsEnabled(getContext()));
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean enabled) {
                updateAlarmSetting(enabled);
            }
        });
        return view;
    }

    private void updateAlarmSetting(boolean enabled) {
        Log.d(TAG, "updateAlarmSetting: ");
        PrefsUtil.setNotificationsEnabled(mCtx, enabled);
        if (enabled) {
            scheduleJob();
            // Send user to phone settings if alarms are disabled for this particular app.
            checkIfUserHasGloballyDisabledNotificationsFromThisApp();
        } else {
            cancelJob();
        }
    }

    /**
     * Check if user has globally disabled notifications published by this app.
     */
    private void checkIfUserHasGloballyDisabledNotificationsFromThisApp() {
        Log.d(TAG, "checkIfUserHasGloballyDisabledNotificationsFromThisApp: ");
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mCtx);
        if (!notificationManagerCompat.areNotificationsEnabled()) {
            openNotificationSettings();
        }
    }

    /**
     * Open phone settings to let user enable notification alerts for this app.
     */
    private void openNotificationSettings() {
        Log.d(TAG, "openNotificationSettings: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, mCtx.getPackageName());
            startActivity(intent);
        } else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + mCtx.getPackageName()));
            startActivity(intent);
        }
    }

    /**
     * Creates a background job that runs appx. every 24 hour.
     * The job will make a http call to fetch the latest xkcd number and show a notification
     * if the the number is new (does not equal the number when last check was done)
     */
    private void scheduleJob() {
        Log.d(TAG, "scheduleJob: ");
        ComponentName componentName = new ComponentName(mCtx, DailyReminderService.class);
        JobInfo info = new Builder(JOB_ID, componentName)
                .setRequiredNetworkType(NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(ONE_DAY_INTERVAL) // min 15 min
                .build();
        JobScheduler schedular = (JobScheduler) mCtx.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int resultCode = schedular.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job schedululing failed");
        }
    }

    /**
     * Cancel the alarm
     */
    private void cancelJob() {
        Log.d(TAG, "cancelJob: Job canceled");
        JobScheduler scheduler = (JobScheduler) mCtx.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(JOB_ID);
    }
}
