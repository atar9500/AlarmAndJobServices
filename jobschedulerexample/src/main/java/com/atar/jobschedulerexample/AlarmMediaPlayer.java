package com.atar.jobschedulerexample;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.Settings;

/**
 * We decided to put the MediaPlayer as a static variable in a separated class
 * in order to control it in multiple activities.
 */
public class AlarmMediaPlayer {

    public static MediaPlayer player;

    /**
     * A static method that plays the notification sound.
     * @param context If the MediaPlayer variable is null it'll be generated from this context.
     */
    public static void play(Context context){
        if(player == null){

            // Creating the MediaPlayer and setting the alarm sound from system's default.
            player = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        player.start();
    }

    /**
     * A static method that stops the notification sound to play if the Media Player variable is null.
     */
    public static void stop(){
        if(player != null){
            player.stop();
        }
    }

}
