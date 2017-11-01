package com.atar.jobintervalexample;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.widget.Toast;

public class MyJobService extends JobService implements PlayerCallback{

    /**
     * An Async-Task that does something, keep in mind that in order to
     * long running tasks such as contacting servers and etc. you'll need
     * an Async-Task for that.
     */
    private PlayerTask mTask;

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
        mTask = new PlayerTask(this);
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
     * This method plays the default notification sound.
     * Called in the Async-Task's doInBackground().
     */
    @Override
    public void onPlayingSound() {
        AlarmMediaPlayer.play(this);
    }

    /**
     * An overridden method by the PlayerCallback interface that I created.
     * This method shows a Toast with the input that the user wrote.
     * Called in the Async-Task's onPostExecute().
     */
    @Override
    public void onTaskFinished(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

        /*
         * It's important to notify when the job is finished,
         * we'll need to pass the Job Parameters as well as if the
         * job need reschedule (true) or not (false).
         */
        jobFinished(mParam, false);
    }
}
