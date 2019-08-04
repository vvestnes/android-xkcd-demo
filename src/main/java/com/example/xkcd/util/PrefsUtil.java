package com.example.xkcd.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class PrefsUtil {

    private static final String SETTING_NOTIFICATIONS_ALARM = "SETTING_NOTIFICATIONS_ALARM";
    private static final String CURRENT_NUM = "CURRENT_NUM";

    private static final String TAG = "PrefsUtil";

    private static SharedPreferences getPrefs(Context ctx) {
        Log.d(TAG, "getPrefs: ");
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    /*
    private static String getPrefString(Context ctx, String key, String def) {
        Log.d(TAG, "getPrefString: " + key);
        return getPrefs(ctx).getString(key, def);
    }
    */

    private static int getPrefInt(Context ctx, String key) {
        Log.d(TAG, "getPrefInt: " + key);
        return getPrefs(ctx).getInt(key, -1);
    }

    public static void setCurrent(Context ctx, int val) {
        Log.d(TAG, "setCurrent: " + val);
        getPrefs(ctx).edit().putInt(CURRENT_NUM, val).apply();
    }

    public static int getCurrent(Context ctx) {
        Log.d(TAG, "getCurrent: ");
        return getPrefInt(ctx, CURRENT_NUM);
    }

    public static boolean isNotificationsEnabled(Context ctx) {
        return getPrefs(ctx).getBoolean(SETTING_NOTIFICATIONS_ALARM, false);
    }

    public static void setNotificationsEnabled(Context ctx, boolean enabled) {
        getPrefs(ctx).edit().putBoolean(SETTING_NOTIFICATIONS_ALARM, enabled).apply();
    }

}
