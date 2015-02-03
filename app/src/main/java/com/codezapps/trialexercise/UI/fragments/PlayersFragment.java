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

import java.util.ArrayList;

public class PlayersFragment extends ListFragment {

    public static final String TAG_PLAYERS = "players";

    private ArrayList<Player> mPlayers;

    public static PlayersFragment newInstance() {
        PlayersFragment fragment = new PlayersFragment();
        return fragment;
    }

    public PlayersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPlayers = new ArrayList<Player>();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setEmptyText("No players found");
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mCallback) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            if(position==mPlayers.size()) mCallback.fetchMore(PlayersAdapter.PLAYERS,mPlayers.size());
            else mCallback.onItemClick(mPlayers.get(position).getName());
        }
    }

    public ArrayList<Player> getDataCollection(){
        return mPlayers;
    }

    public String getName(){
        return "Players";
    }

    public BaseAdapter initAdapter()
    {
        return new PlayersAdapter(getActivity(),mPlayers);
    }

    public void handleInitialRequest(JSONHolder jsonHolder){
        mPlayers.addAll(jsonHolder.getPlayers());
        mFooterView.setVisibility(View.VISIBLE);
    }

    public void handleSearchRequest(JSONHolder jsonHolder){
        mPlayers.clear();
        mPlayers.addAll(jsonHolder.getPlayers());
        if(mPlayers.size()<10) mFooterView.setVisibility(View.INVISIBLE);
    }

    public void showMore(JSONHolder jsonHolder,int position)   {
        if(position==PlayersAdapter.PLAYERS && jsonHolder.hasPlayers()) {
            mPlayers.addAll(jsonHolder.getPlayers());
            if(mPlayers.size()%10!=0) mFooterView.setVisibility(View.INVISIBLE);
        }

        else {
            Toast.makeText(getActivity(), "No players were fetched", Toast.LENGTH_SHORT).show();
            mFooterView.setVisibility(View.INVISIBLE);
        }

    }
}
