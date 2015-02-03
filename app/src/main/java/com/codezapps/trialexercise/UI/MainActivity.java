package com.codezapps.trialexercise.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codezapps.trialexercise.Common.IActivity;
import com.codezapps.trialexercise.Common.IWorkerCallback;
import com.codezapps.trialexercise.Common.JSONHolder;
import com.codezapps.trialexercise.Common.QueryTimer;
import com.codezapps.trialexercise.Common.Worker;
import com.codezapps.trialexercise.Model.CommonObject;
import com.codezapps.trialexercise.Model.Player;
import com.codezapps.trialexercise.Model.Team;
import com.codezapps.trialexercise.R;
import com.codezapps.trialexercise.UI.ListAdapter;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements IActivity,IWorkerCallback {

    public static final String ACTIVITY_ARGS="args";

    public static final boolean LOGD = false;

    private ExpandableListView mExpandableListView;
    private ListAdapter mAdapter;
    private QueryTimer mQueryTimer;

    private SearchView mSearchView;
    private String mSearchArgs="a";

    private ArrayList<Player> mPlayersArray;
    private ArrayList<Team> mTeamsArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        miniSetup();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayersArray = new ArrayList<Player>();
        mTeamsArray = new ArrayList<Team>();
        mQueryTimer = QueryTimer.newInstance(this);
        initExpListView(mPlayersArray, mTeamsArray);
        initialPull();
    }

    private void miniSetup(){
        if (android.os.Build.VERSION.SDK_INT > 10) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
                policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                        .detectDiskReads().detectDiskWrites().detectNetwork()
                        .penaltyFlashScreen().build();
            StrictMode.setThreadPolicy(policy);
        }
            supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setSupportProgressBarIndeterminateVisibility(true);

    }

    private void initExpListView(ArrayList<Player> players,ArrayList<Team> teams)
    {
        ExpandableListView mExpandableListView = (ExpandableListView) findViewById(R.id.listView);

        mAdapter = new ListAdapter(this,players,teams);

        mExpandableListView.setAdapter(mAdapter);

        mExpandableListView.setGroupIndicator(null);

        mExpandableListView.expandGroup(0);
        mExpandableListView.expandGroup(1);

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // Doing nothing
                return true;
            }
        });
        mExpandableListView.setEmptyView(findViewById(R.id.empty));

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                clickOn(groupPosition,childPosition);
                return true;
            }
        });
    }

    private void clickOn(int groupPosition,int childPosition){
        CommonObject obj = (CommonObject) mAdapter.getChild(groupPosition,childPosition);
        Intent intent = new Intent(getBaseContext(),SimpleActivity.class);
        intent.putExtra(ACTIVITY_ARGS,obj.getName());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat
                .getActionView(searchItem);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String arg0) {
                mSearchArgs = arg0;
                mQueryTimer.startTimer();
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                return false;

            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                initialPull();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.action_search:
                mSearchView.setIconified(false);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void initialPull(){
        String[] params={mSearchArgs,"","0"};
        new Worker(this,Worker.INITIAL_REQUEST).execute(params);
    }

     public void fetchMore(int position,int offset){
         String searchType="";
         searchType += (position==0) ? "players" : "teams";
         String[] params={mSearchArgs,searchType,offset+""};
         new Worker(this,Worker.SHOWMORE_REQUEST).execute(params);
    }

    public void postFinished(JSONHolder jsonHolder,int requestType)
    {
        switch(requestType) {
            case Worker.INITIAL_REQUEST:
                mPlayersArray.addAll(jsonHolder.getPlayers());
                mTeamsArray.addAll(jsonHolder.getTeams());
                break;
            case Worker.SEARCH_REQUEST:
                handleSearchRequest(jsonHolder);
                break;
            case Worker.SHOWMORE_REQUEST:
                showMore(jsonHolder);
                break;
            default:
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setIndeterminateProgress(boolean bool){
        setSupportProgressBarIndeterminateVisibility(true);
    }

    public String getSearchArgs(){
        return mSearchArgs;
    }

    public void handleSearchRequest(JSONHolder jsonHolder)
    {
        mPlayersArray.clear();
        mTeamsArray.clear();
        mPlayersArray.addAll(jsonHolder.getPlayers());
        mTeamsArray.addAll(jsonHolder.getTeams());
    }

    public void showMore(JSONHolder jsonHolder){
        if(jsonHolder.hasPlayers()) mPlayersArray.addAll(jsonHolder.getPlayers());
        else Toast.makeText(this,"No players were fetched",Toast.LENGTH_SHORT).show();
        if(jsonHolder.hasTeams()) mTeamsArray.addAll(jsonHolder.getTeams());
            else Toast.makeText(this,"No teams were fetched",Toast.LENGTH_SHORT).show();
    }
  }
