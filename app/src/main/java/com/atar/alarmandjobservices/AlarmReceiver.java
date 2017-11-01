package com.atar.alarmandjobservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    /**
     * onReceive is triggered by the time we set in the Activity to be triggered.
     * @param context The context we passed in the Pending Intent when we set the Alarm,
     *                in our case MainActivity's context
     * @param intent The context we passed in the Pending Intent when we set the Alarm
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        /*
         * Right when onReceiver is called, the MediaPlayer is set with an Alarm Sound.
         * We decided to put the MediaPlayer as a static variable in a separated class
         * in order to control it in multiple activities.
         */
        AlarmMediaPlayer.play(context);

        /*
         * When the broadcast Receiver has been triggered, the Main Activity will be started.
         * In order to do that first we'll need an intent from the context variable to the
         * MainActivity class.
         */
        Intent openActivity = new Intent(context.getApplicationContext(), MainActivity.class);

        // In case the activity is already opened, we'll put those flags in order to avoid open it up again.
        openActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(openActivity);
    }
}
