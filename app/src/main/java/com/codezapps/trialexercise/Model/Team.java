package com.codezapps.trialexercise.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by viperime on 2/2/2015.
 */
public class Team extends CommonObject{
    public static final String ID = "teamID";
    public static final String NAME= "teamName";
    public static final String STADIUM ="teamStadium";
    public static final String NATION = "isNation";
    public static final String NATIONALITY = "teamNationality";
    public static final String CITY = "teamCity";

    private int mID;
    private String mName;
    private String mStadium;
    private boolean mNation;
    private String mNationality;
    private String mCity;

    public Team(JSONObject object) throws JSONException{
        super(object.getString(NAME));
        mID = object.getInt(ID);
        mStadium = object.getString(STADIUM);
        mNation = object.getBoolean(NATION);
        mNationality = object.getString(NATIONALITY);
        mCity = object.getString(CITY);
    }

    public Team(){
        super("MyTeam");
        mID=1;
        mStadium = "MyStadium";
        mNation = false;
        mNationality="None";
        mCity = "Braila";
    }

    public int getID() {
        return mID;
    }

    public String getStadium() {
        return mStadium;
    }

    public boolean isNation() {
        return mNation;
    }

    public String getNationality() {
        return mNationality;
    }

    public String getCity() {
        return mCity;
    }

}
