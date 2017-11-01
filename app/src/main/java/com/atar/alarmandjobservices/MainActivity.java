package com.atar.alarmandjobservices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    /**
     * UI WIDGETS
     */
    private TimePicker mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //A private method to initialize the UI Widgets
        initUIWidgets();
    }

    /**
     * A method to initialize the UI Widgets
     */
    private void initUIWidgets(){

        //Setting up the time picker
        mTimePicker = findViewById(R.id.time_picker);

        //Setting up the SET button
        FloatingActionButton set = findViewById(R.id.set_btn);

        //Setting up the click listener for the SET button
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                 * When clicking the SET button, the activity will gather up the current
                 * date, as well as the current hour and time.
                 */
                Calendar calendar = Calendar.getInstance();
                if(Build.VERSION.SDK_INT >= 23){
                    calendar.set(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            mTimePicker.getHour(),
                            mTimePicker.getMinute(),
                            0);
                } else {

                    /*
                     * Since API 23 .getCurrentHour() and .getCurrentMinute() are deprecated,
                     * but still it's only available to use .getHour() and .getCurrentMinute()
                     * since API 23 and above.
                     */
                    calendar.set(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute(),
                            0);
                }

                //When all is set and done, it directs to a method where that alarm is set
                setAlarm(calendar.getTimeInMillis());
            }
        });

        //Setting up the STOP button
        FloatingActionButton stop = findViewById(R.id.stop_btn);

        //Setting up the Click Listener for the STOP button
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                 * When clicking the Stop button, the Alarm Sound will stop playing
                 * and the Stop button will disappear.
                 */
                AlarmMediaPlayer.stop();
                ((FloatingActionButton)view).hide();
                view.setClickable(false);
            }
        });
        if(!AlarmMediaPlayer.isPlaying()){

            /*
             * When the Alarm sound is not playing, the STOP button has no use
             * so it needs to begone.
             */
            stop.hide();
            stop.setClickable(false);
        }
    }

    /**
     * The method creates an Alarm Manager and sets the alarm to the exact time the user
     * decided it to be.
     * @param alarm the time in milliseconds the user set in the time picker
     */
    public void setAlarm(long alarm) {

        //Creating the Alarm Manager from the System Services
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        //Setting up an intent that goes from this context straight to the Broadcast Receiver class
        Intent intent = new Intent(this, AlarmReceiver.class);

        //Creating a Pending Intent and implementing it this context as well as the intent we created
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        //Setting the AlarmManager to trigger the Broadcast Receiver every 24 hours
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm, AlarmManager.INTERVAL_DAY, pendingIntent);

        //Showing up a Toast to make sure everything is cool
        Toast.makeText(this, "Alarm is set!", Toast.LENGTH_SHORT).show();
    }
}
