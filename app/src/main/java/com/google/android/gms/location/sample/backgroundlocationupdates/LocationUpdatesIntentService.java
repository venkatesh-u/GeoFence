/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.location.sample.backgroundlocationupdates;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import java.util.List;

/**
 * Handles incoming location updates and displays a notification with the location data.
 *
 * For apps targeting API level 25 ("Nougat") or lower, location updates may be requested
 * using {@link android.app.PendingIntent#getService(Context, int, Intent, int)} or
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)}. For apps targeting
 * API level O, only {@code getBroadcast} should be used.
 *
 *  Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 *  less frequently than the interval specified in the
 *  {@link com.google.android.gms.location.LocationRequest} when the app is no longer in the
 *  foreground.
 */
public class LocationUpdatesIntentService extends IntentService {

    static final String ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.backgroundlocationupdates.action" +
                    ".PROCESS_UPDATES";
    private static final String TAG = LocationUpdatesIntentService.class.getSimpleName();


    public LocationUpdatesIntentService() {
        // Name the worker thread.
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
      Log.d(TAG,"onHandleIntent 1")  ;
        if (intent != null) {
            Log.d(TAG,"onHandleIntent 2")  ;

            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                Log.d(TAG,"ACTION_PROCESS_UPDATES ")  ;

                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    Log.d(TAG,"LocationResult")  ;

                    List<Location> locations = result.getLocations();
                    LocationResultHelper locationResultHelper = new LocationResultHelper(this, locations.get(0));
                    // Save the location data to SharedPreferences.
                    locationResultHelper.saveResults();
                    // Show notification with the location data.
                    //locationResultHelper.showNotification();
                    Log.i(TAG, LocationResultHelper.getSavedLocationResult(this));

                    // Creating an intent for broadcastreceiver
                    Intent broadcastIntent = new Intent(Constants.BROADCAST_ACTION);
                    // Attaching data to the intent
                    broadcastIntent.putExtra(Constants.EXTENDED_DATA_STATUS, result);
                    // Sending the broadcast
                    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                }
            }
        }
    }
}

