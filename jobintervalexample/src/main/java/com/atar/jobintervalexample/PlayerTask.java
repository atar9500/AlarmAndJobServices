package com.atar.jobintervalexample;

import android.os.AsyncTask;

public class PlayerTask extends AsyncTask<String, Void, String> {

    /**
     * Callback field in order to contact the Job Service.
     */
    private PlayerCallback mCallback;

    /**
     * A constructor that gets the callback from the Job Service.
     * @param callback Job Service's callback.
     */
    public PlayerTask(PlayerCallback callback){
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {

        // Making the Job Service to play a sound.
        mCallback.onPlayingSound();
        return strings[0];
    }

    @Override
    protected void onPostExecute(String s) {

        // Triggering an overridden method by the callback in the Job Service.
        mCallback.onTaskFinished(s);
    }
}
