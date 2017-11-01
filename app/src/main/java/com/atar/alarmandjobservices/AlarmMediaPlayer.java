package com.atar.alarmandjobservices;

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
     * A static method that plays the alarm sound.
     * @param context If the MediaPlayer variable is null it'll be generated from this context.
     */
    public static void play(Context context){
        if(player == null){

            // Creating the MediaPlayer and setting the alarm sound from system's default.
            player = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        }
        player.start();

        // We'll loop the hell out of this sound.
        player.setLooping(true);
    }

    /**
     * A static method that stops the Alarm Sound to play if the Media Player variable is null.
     */
    public static void stop(){
        if(player != null){
            player.stop();
        }
    }

    /**
     * A static method that return true if the Media Player variable is playing or false if not
     * or if the Media Player variable is null.
     */
    public static boolean isPlaying(){
        if(player == null){
            return false;
        } else {
            return player.isPlaying();
        }
    }

}
