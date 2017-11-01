package com.atar.jobschedulerexample;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

public class MyJobService extends JobService implements TaskCallback {

    public static final String BROADCAST_IDENTIFIER_FOR_SERVICE_FINISHED_RESPONSE =
            "com.atar.jobschedulerexample.MyJobService.IdentifierForMyJobService";

    public static final String NOTIFICATION_CHANNEL_ID = "4565";

    /**
     * An Async-Task that does something, keep in mind that in order to
     * long running tasks such as contacting servers and etc. you'll need
     * an Async-Task for that.
     */
    private SampleTask mTask;

    /**
     * A field for the Job Parameters, in order to finish the job outside
     * of the overridden methods of the JobService.
     */
    private JobParameters mParam;

    /**
     * When it's job's time to be executed, this method will be triggered.
     * @param jobParameters The job parameters that we passed from that MainActivity.
     * @return When the method is finished, it should notify whether the job
     *         is done (return true) or continues (return false);
     */
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // Getting the user's message to a String
        String msg = jobParameters.getExtras().getString("MSG");

        // Passing the Job Parameters into a field.
        mParam = jobParameters;

        // Executing the Async-Task that does things.
        mTask = new SampleTask(this);
        mTask.execute(msg);

        // Returning true, the job isn't finished yet.
        return true;
    }

    /**
     * When the job is being interrupted by anything (Lower battery, no internet connection and etc.)
     * this method is called.
     * @param jobParameters The job parameters that we passed from that MainActivity.
     * @return When the method is finished, it should notify whether the job
     *         is done (return true) or continues (return false);
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        // If the Async-Task is not null, the Async-Task is being cancelled in order to save performance.
        if(mTask != null){
            mTask.cancel(true);
        }
        return true;
    }

    /**
     * An overridden method by the PlayerCallback interface that I created.
     * This method shows a Notification with the input that the user wrote
     * and send a broadcast to its broadcast receivers.
     * Called in the Async-Task's onPostExecute().
     * @param s A string that generated from the Async-Task.
     */
    @Override
    public void onTaskFinished(String s) {

        /*
         * It's important to notify when the job is finished,
         * we'll need to pass the Job Parameters as well as if the
         * job need reschedule (true) or not (false).
         */
        jobFinished(mParam, false);

        // Setting up an intent to MainActivity.
        Intent intent = new Intent(this , MainActivity.class);
        intent.putExtra("MSG", s);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Inserting the intent inside a Pending Intent.
        PendingIntent resultIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Setting the notification alert sound to system's default.
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Building a notification
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Job Completed")
                .setContentText(s)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());

        // Notifying all the receivers that are subscribed to this service.
        Intent broadcastIntent = new Intent(BROADCAST_IDENTIFIER_FOR_SERVICE_FINISHED_RESPONSE);
        broadcastIntent.putExtra("MSG", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

    }
}
