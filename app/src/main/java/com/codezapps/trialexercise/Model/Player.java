package com.codezapps.trialexercise.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by viperime on 2/2/2015.
 */
public class Player extends  CommonObject{

    public static final String ID = "playerID";
    public static final String NAME= "playerSecondName";
    public static final String NATIONALITY = "playerNationality";
    public static final String AGE ="playerAge";
    public static final String CLUB = "playerClub";

    private int mID;
    private String mName;
    private String mNationality;
    private int mAge;
    private String mClub;

    public Player(JSONObject object) throws JSONException{
        super(object.getString(NAME));
        mID = object.getInt(ID);
        mNationality = object.getString(NATIONALITY);
        try{
        mAge = Integer.valueOf(object.getString(AGE));}
        catch(NumberFormatException ex){
            mAge = 0;
        }
        mClub = object.getString(CLUB);
    }

    public Player()
    {
        super("Claudiu Manea");
        mID=1;
        mNationality = "Romanian";
        mAge = 25;
        mClub = "None";
    }

    public int getID() {
        return mID;
    }

    public String getNationality() {
        return mNationality;
    }

    public int getAge() {
        return mAge;
    }

    public String getClub() {
        return mClub;
    }


}
