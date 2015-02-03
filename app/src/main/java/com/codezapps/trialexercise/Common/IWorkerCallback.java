package com.codezapps.trialexercise.Common;

/**
 * Created by viperime on 2/3/2015.
 */
public interface IWorkerCallback {
    void postFinished(JSONHolder jsonHolder,int request);
    String getSearchArgs();
    void setIndeterminateProgress(boolean visible);
}
