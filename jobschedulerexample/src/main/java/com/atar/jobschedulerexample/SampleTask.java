package com.atar.jobschedulerexample;

import android.os.AsyncTask;

import java.util.Calendar;

public class SampleTask extends AsyncTask<String, Void, String> {

    /**
     * Callback field in order to contact the Job Service.
     */
    private TaskCallback mCallback;

    /**
     * A constructor that gets the callback from the Job Service.
     * @param callback Job Service's callback.
     */
    public SampleTask(TaskCallback callback){
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {

        // Getting the current hour in a string
        Calendar now = Calendar.getInstance();
        String hour = now.get(Calendar.HOUR) + "";
        if(hour.length() == 1){
            hour += '0';
        }
        String min = now.get(Calendar.MINUTE) + "";
        if(min.length() == 1){
            min += '0';
        }

        // Passing user's message + the current hour.
        return strings[0] + " - " + hour + ":" + min;
    }

    @Override
    protected void onPostExecute(String s) {

        // Triggering an overridden method by the callback in the Job Service.
        mCallback.onTaskFinished(s);
    }
}
