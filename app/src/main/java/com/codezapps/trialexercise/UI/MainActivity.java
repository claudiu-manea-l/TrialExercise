package com.codezapps.trialexercise.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codezapps.trialexercise.UI.fragments.ListFragment;
import com.codezapps.trialexercise.UI.fragments.PagerFragment;
import com.codezapps.trialexercise.UI.fragments.PlayersAdapter;
import com.codezapps.trialexercise.UI.fragments.PlayersFragment;
import com.codezapps.trialexercise.UI.fragments.TeamsAdapter;
import com.codezapps.trialexercise.UI.fragments.TeamsFragment;
import com.codezapps.trialexercise.common.IActivity;
import com.codezapps.trialexercise.common.IWorkerCallback;
import com.codezapps.trialexercise.common.JSONHolder;
import com.codezapps.trialexercise.common.QueryTimer;
import com.codezapps.trialexercise.common.Worker;
import com.codezapps.trialexercise.model.CommonObject;
import com.codezapps.trialexercise.model.Player;
import com.codezapps.trialexercise.model.Team;
import com.codezapps.trialexercise.R;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements IActivity,IWorkerCallback {

    public static final String ACTIVITY_ARGS="args";

    public static final boolean LOGD = false;

    private int mOffset=0;
    private PagerFragment mPagerFragment;

    private QueryTimer mQueryTimer;

    private SearchView mSearchView;
    private String mSearchArgs="a";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        miniSetup();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueryTimer = QueryTimer.newInstance(this);

        mPagerFragment = PagerFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame,mPagerFragment).commit();

        if(isNetworkOnline()) initialPull();
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


    public void onItemClick(String name){
        Intent intent = new Intent(getBaseContext(),SimpleActivity.class);
        intent.putExtra(ACTIVITY_ARGS,name);
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
                return false;
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
        String[] params={"a","","0"};
        new Worker(this,Worker.INITIAL_REQUEST).execute(params);
        if(mOffset>0)
        for(int i=0;i<2;i++)
        {ListFragment frag = mPagerFragment.getFragment(i);
            if(frag!=null) frag.setProgressIndeterminate();}
        mOffset++;
    }

     public void fetchMore(int position,int offset){
         String searchType="";
         searchType += (position==0) ? "players" : "teams";
         String[] params={mSearchArgs,searchType,offset+""};
         System.out.println(params.toString());
         new Worker(this,Worker.SHOWMORE_REQUEST,position).execute(params);
    }

    public void postFinished(JSONHolder jsonHolder,int requestType,int position)
    {
            mPagerFragment.getFragment(PlayersAdapter.PLAYERS)
                    .handleWorkerPost(jsonHolder, requestType, position);
            mPagerFragment.getFragment(TeamsAdapter.TEAMS)
                    .handleWorkerPost(jsonHolder, requestType, position);
    }

    public String getSearchArgs(){
        return mSearchArgs;
    }

    public boolean isNetworkOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }



}
