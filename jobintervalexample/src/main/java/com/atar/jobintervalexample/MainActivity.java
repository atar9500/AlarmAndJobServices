package com.atar.jobintervalexample;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /**
     * The Job ID. Please note that if you have more than on job
     * to schedule, you'll need a unique ID for each job.
     */
    private static final int JOB_ID = 12;

    // The JobScheduler field
    private JobScheduler mScheduler;

    /**
     * UI Widgets
     */
    private FloatingActionButton mStart, mStop;
    private TextView mStatus;
    private EditText mMessageWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        initUIWidgets();

    }

    /**
     * A method to initialize the UI Widgets
     */
    private void initUIWidgets(){

        // Setting up the START_JOB button.
        mStart = findViewById(R.id.start_btn);

        // Setting the click listener for the START_JOB button.
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleJob();
            }
        });

        // If the job is currently running, there is no need in this button so it'll be hidden.
        if(getJobStatus()){
           mStart.hide();
           mStart.setClickable(false);
        }

        // Setting up the STOP_JOB button.
        mStop = findViewById(R.id.stop_btn);

        // Setting the click listener for the STOP_JOB button.
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelJob();
            }
        });

        // If there's no job running, there is no need in this button so it'll be hidden.
        if(!getJobStatus()){
            mStop.hide();
            mStop.setClickable(false);
        }

        // Setting up the TextView that shows the job's status at the moment.
        mStatus = findViewById(R.id.job_status);

        /*
         * If the job is running, the TextView which will be shown is "Job is running!",
         * else it'll show "Job is not running..."
         */
        if(getJobStatus()){
            mStatus.setText("Job is running!");
        } else {
            mStatus.setText("Job is not running...");
        }

        // Setting up the EditText in which the user can set up data to the JobService.
        mMessageWriter = findViewById(R.id.msg_writer);
    }

    /**
     * A method which is used to gather all the needed data for scheduling a job
     */
    private void scheduleJob(){

        // In PersistableBundle we'll pass all the additional data
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("MSG", mMessageWriter.getText().toString());

        /*
         * In the component name we'll mention from which context to which JobService
         * we'll want to pass the info to.
         */
        ComponentName name = new ComponentName(this, MyJobService.class);

        /*
         * In JobInfo we define all the requirement and conditions in which we'll want our job to schedule.
         * Please note that if you have more than on job to schedule, you'll need a unique ID for each job.
         */
        JobInfo info = new JobInfo.Builder(JOB_ID, name)

                /*
                 * The job will be executed every 3 seconds.
                 * Keep in mind when setting a repeating job, you cannot set deadline,
                 * and minimum delay latency as they will throw an exception.
                 * Also, requirements such as Network, Charged, Not Low Battery Level and etc. will
                 * be ignored when setting periodic time.
                 */
                .setPeriodic(3000)

                /*
                 * The job will continue even after device reboot.
                 * In addition you'll need to add RECEIVE_BOOT_COMPLETED in the manifest.
                 */
                .setPersisted(true)

                // This where we pass the data to the JobInfo.
                .setExtras(bundle)
                .build();

        // All our info is passed into the JobScheduler
        mScheduler.schedule(info);

        // Saving in shared preference that we scheduled a job.
        saveJobStatus(true);

        // Hiding the START_JOB button.
        mStart.hide();
        mStart.setClickable(false);

        // Showing the STOP_JOB button.
        mStop.show();
        mStop.setClickable(true);

        // Updating the TextView status about the current status of the job.
        mStatus.setText("Job is running!");
    }

    private void cancelJob(){

        // Stopping the static Media Player.
        AlarmMediaPlayer.stop();

        // Canceling the job by passing its ID
        mScheduler.cancel(JOB_ID);

        // Saving the current job status to Shared Preference.
        saveJobStatus(false);

        // Hiding the STOP_JOB button.
        mStop.hide();
        mStop.setClickable(false);

        // Showing the START_JOB button.
        mStart.show();
        mStart.setClickable(true);

        // Updating the TextView status about the current status of the job.
        mStatus.setText("Job is not running...");
    }

    /**
     * Saving the current job status to a Shared Preference.
     * @param status current job satus, true for running and false for not running.
     */
    private void saveJobStatus(boolean status){
        SharedPreferences settingsFile = getSharedPreferences("STATUS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settingsFile.edit();
        editor.putBoolean("RUNNING", status);
        editor.apply();
    }

    /**
     * A method that return the current job status
     * @return returning the current job status, true for running and false for not running.
     */
    private boolean getJobStatus(){
        SharedPreferences settingsFile = getSharedPreferences("STATUS", Context.MODE_PRIVATE);
        return settingsFile.getBoolean("RUNNING", false);
    }

}
