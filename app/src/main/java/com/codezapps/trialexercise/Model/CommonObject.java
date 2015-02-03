package com.codezapps.trialexercise.model;

/**
 * Created by viperime on 2/2/2015.
 */
public abstract class CommonObject {

    private String mName;

    protected CommonObject(String name)
    {
        mName = name;
    }

    public String getName(){
        return mName;
    }
}
