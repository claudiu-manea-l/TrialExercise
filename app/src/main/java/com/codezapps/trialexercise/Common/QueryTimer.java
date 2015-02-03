package com.codezapps.trialexercise.common;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by viperime on 2/2/2015.
 */
public class QueryTimer {

    private final int TIMER_DELAY = 400;

    private static QueryTimer mInstance;

    private Timer mTimer;
    private TimerTask task;

    private IWorkerCallback mCallback;
    private Worker mWorker;


    private int mTimerState;
    private final int STATE_IDLE = 0;
    private final int STATE_SCHEDULED = 1;
    private final int STATE_POOLING = 2;
    private final int STATE_FINISHED =3;

    public static QueryTimer newInstance(IWorkerCallback callback)
    {
        if(mInstance==null) mInstance = new QueryTimer(callback);
        return mInstance;
    }

    private QueryTimer(IWorkerCallback callback) {
        mCallback = callback;
    }

    public void startTimer()
    {
        mTimer = new Timer();
        switch(mTimerState) {
            case STATE_IDLE:
                mTimer.schedule(new RemindTask(), TIMER_DELAY);
                mTimerState = STATE_SCHEDULED;
                break;
            case STATE_SCHEDULED:
                resetTimer();
                break;
            case STATE_POOLING:
                cancelTask();
                break;

        }

    }

    private void resetTimer()
    {
        mTimerState = STATE_IDLE;
        mTimer.cancel();
        mTimer = null;
        startTimer();
    }

    private void cancelTask()
    {
        mTimerState = STATE_IDLE;
        if(mWorker!=null) mWorker.cancel(true);
        startTimer();
    }

    class RemindTask extends TimerTask {
        @Override
        public void run() {
            String args = mCallback.getSearchArgs();
            String[] params = {args,"","0"};
            mWorker = new Worker(mCallback,Worker.SEARCH_REQUEST);
            mWorker.execute(params);
            mCallback.setIndeterminateProgress(true);
        }
    }
}
