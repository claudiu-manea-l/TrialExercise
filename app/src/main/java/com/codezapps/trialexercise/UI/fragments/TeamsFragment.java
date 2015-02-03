package com.codezapps.trialexercise.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.codezapps.trialexercise.common.JSONHolder;
import com.codezapps.trialexercise.model.Player;
import com.codezapps.trialexercise.model.Team;

import java.util.ArrayList;

/**
 * Created by viperime on 2/3/2015.
 */
public class TeamsFragment extends ListFragment{

    public static final String TAG_TEAMS = "teams";

    private ArrayList<Team> mTeams;

    public static TeamsFragment newInstance() {
        TeamsFragment fragment = new TeamsFragment();
        return fragment;
    }

    public TeamsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTeams = new ArrayList<Team>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setEmptyText("No teams found");
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mCallback) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mCallback.onItemClick(mTeams.get(position).getName());
        }
    }

    @Override
    public void onClick(View v){
        if (null != mCallback)
            mCallback.fetchMore(TeamsAdapter.TEAMS,mTeams.size());
    }

    public ArrayList<Team> getDataCollection(){
        return mTeams;
    }

    public String getName(){
        return "Teams";
    }

    public BaseAdapter initAdapter()
    {
        return new TeamsAdapter(getActivity(),mTeams);
    }

    public void handleInitialRequest(JSONHolder jsonHolder)
    {
        mTeams.addAll(jsonHolder.getTeams());
    }

    public void handleSearchRequest(JSONHolder jsonHolder)
    {
        mTeams.clear();
        mTeams.addAll(jsonHolder.getTeams());
    }

    public void showMore(JSONHolder jsonHolder){
        if(jsonHolder.hasTeams()) mTeams.addAll(jsonHolder.getTeams());
        else Toast.makeText(getActivity(),"No teams were fetched",Toast.LENGTH_SHORT).show();
    }
}
