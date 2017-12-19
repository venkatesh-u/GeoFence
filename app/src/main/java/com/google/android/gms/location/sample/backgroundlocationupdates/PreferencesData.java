package com.google.android.gms.location.sample.backgroundlocationupdates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by venkatesh on 30/10/17.
 */

class PreferencesData {

    public static final String SHARED_PREF="credentials";
    private static SharedPreferences preferences;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PREF_TOKEN="auth_token";


    @SuppressLint("NewApi")
    public static String getRegistrationId(Context con) {
        if(preferences==null)
            preferences=con.getSharedPreferences(SHARED_PREF, 0);
        String registrationId = preferences.getString(PROPERTY_REG_ID, "");
        ////Log.i(TAG, registrationId);
        if (registrationId.isEmpty()) {
            //Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = preferences.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(con);
        if (registeredVersion != currentVersion) {
            //Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String getToken(Context con)
    {
        if(preferences==null)
            preferences = con.getSharedPreferences(SHARED_PREF, 0);
        return preferences.getString(PREF_TOKEN, "");
    }

    public static synchronized void initPrefs(Context context) {
        if (preferences == null) {

            preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        }
    }

}
