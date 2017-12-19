package com.google.android.gms.location.sample.backgroundlocationupdates;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceRegistrationService extends IntentService {

    private static final String TAG = "GeoIntentService";
    public GeofenceRegistrationService() {
        super(TAG);
    }
    SharedPreferences sharedPreferences;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");

        sharedPreferences = getSharedPreferences("location_date_storage", MODE_PRIVATE);
        String areaReqId;
        if (sharedPreferences!=null)
            areaReqId = sharedPreferences.getString("area", "UnknownLoc");
        else
            areaReqId = "UnknownLoc1";

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.d(TAG, "GeofencingEvent error " + geofencingEvent.getErrorCode());
        } else {
            int transaction = geofencingEvent.getGeofenceTransition();

            List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
            Geofence geofence = geofences.get(0);

            if (transaction == Geofence.GEOFENCE_TRANSITION_ENTER ) {
                if ( geofence.getRequestId().equals(Constants.GEOFENCE_ID_TECH_M) ){
                    Log.d(TAG, "You are inside TechM");
                    sendNotification("You are inside TechM");
                }else if ( geofence.getRequestId().equals(areaReqId)){
                    Log.d(TAG, "You are inside "+ areaReqId);
                    sendNotification("You are inside "+ areaReqId);
                }
            } else {
                if( geofence.getRequestId().equals(Constants.GEOFENCE_ID_TECH_M) ){
                    Log.d(TAG, "You are outside TechM");
                    sendNotification("You are outside TechM");
                }else if ( geofence.getRequestId().equals(areaReqId)){
                    Log.d(TAG, "You are outside "+ areaReqId);
                    sendNotification("You are outside "+ areaReqId);
                }
            }
        }
    }

    public void sendNotification(String msg) {
        Log.d(TAG, "sendNotification");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("GEO FENCING ALERT");
        builder.setContentText(msg);
        //builder.setSubText("Tap to view the website.");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());

        //Send sms to the device
        sendMsg(msg);
    }


    public void sendMsg(String message){
        String msg = "android.telephony.SmsManager.STATUS_ON_ICC_SENT";
        PendingIntent piSent = PendingIntent.getBroadcast(this, 0,new Intent(msg), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("5556", null, message, piSent, null);
    }

}
