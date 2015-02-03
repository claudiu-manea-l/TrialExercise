package com.codezapps.trialexercise.common;

/**
 * Created by viperime on 2/3/2015.
 */
public interface IWorkerCallback {
    void postFinished(JSONHolder jsonHolder,int request,int position);
    String getSearchArgs();
}
