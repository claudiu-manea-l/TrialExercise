package com.codezapps.trialexercise.common;

import android.util.Log;

import com.codezapps.trialexercise.model.Player;
import com.codezapps.trialexercise.model.Team;
import com.codezapps.trialexercise.UI.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by viperime on 2/2/2015.
 */
public class JSONHolder {

    public static final String PLAYERS = "players";
    public static final String TEAMS = "teams";

    private ArrayList<Player> mPlayers;
    private ArrayList<Team> mTeams;

    public JSONHolder() {
        mPlayers = new ArrayList<Player>();
        mTeams = new ArrayList<Team>();
    }

    public ArrayList<Player> getPlayers(){
        return mPlayers;
    }

    public ArrayList<Team> getTeams(){
        return mTeams;
    }

    public boolean hasPlayers(){
        return !mPlayers.isEmpty();
    }

    public boolean hasTeams(){
        return !mTeams.isEmpty();
    }

    public void parse(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject resultObject = jsonObject.getJSONObject("result");
            parsePlayersArray(resultObject.getJSONArray(PLAYERS));
            parseTeamsArray(resultObject.getJSONArray(TEAMS));
        }
        catch (JSONException ex){
            if(MainActivity.LOGD)
                Log.d("JSONHolder", "JSONException on parse");
                ex.printStackTrace();
        }
    }

    private void parsePlayersArray(JSONArray array) throws JSONException
    {
        for(int i=0;i<array.length();i++)
            mPlayers.add(new Player(array.getJSONObject(i)));
    }

    private void parseTeamsArray(JSONArray array) throws JSONException
    {
        for(int i=0;i<array.length();i++)
            mTeams.add(new Team(array.getJSONObject(i)));
    }

}

