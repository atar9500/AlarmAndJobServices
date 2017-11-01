package com.atar.jobschedulerexample;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    /**
     * The Job ID. Please note that if you have more than on job
     * to schedule, you'll need a unique ID for each job.
     */
    private static final int JOB_ID = 12;

    private class JobReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            // Checking if the intent isn't null.
            if(intent != null){

                String s = intent.getStringExtra("MSG");

                mStatus.setText(s);
            }
        }
    }

    // The JobScheduler field
    private JobScheduler mScheduler;

    // Receiver field
    private JobReceiver mReceiver;

    /**
     * UI Widgets
     */
    private FloatingActionButton mStart;
    private TextView mStatus;
    private EditText mMessageWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        initUIWidgets();
    }

    @Override
    protected void onResume() {

        // Every time this Activity is up, the receiver will be registered to our Job Service.
        mReceiver = new JobReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver
                (mReceiver, new IntentFilter(MyJobService.BROADCAST_IDENTIFIER_FOR_SERVICE_FINISHED_RESPONSE));
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Every time the Activity is paused, the receiver will be unregistered from the JobService.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
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

        // Setting up the TextView that shows the job's status at the moment.
        mStatus = findViewById(R.id.job_status);

        /*
         * When user clicks the notification created by the Job Service,
         * this Activity will get the generated message from the Job Service and display it on the TextView.
         */
        if(getIntent() != null){

            String s = getIntent().getStringExtra("MSG");

            if(s != null && !s.equals("")){
                mStatus.setText(getIntent().getStringExtra("MSG"));
            }
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
         * Sending two jobs with the same ID will cause override of the first one by the second one.
         */
        JobInfo info = new JobInfo.Builder(JOB_ID, name)

                // The job will be executed when there's Wi-Fi connection.
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)

                //
                .setMinimumLatency(60000)

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

        // Updating the TextView status about the current status of the job.
        mStatus.setText("A job is running!");
    }
}
