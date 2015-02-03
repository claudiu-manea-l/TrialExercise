package com.codezapps.trialexercise.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codezapps.trialexercise.Common.FlagName;
import com.codezapps.trialexercise.Common.IActivity;
import com.codezapps.trialexercise.Model.Player;
import com.codezapps.trialexercise.Model.Team;
import com.codezapps.trialexercise.R;

import java.util.ArrayList;

/**
 * Created by viperime on 2/2/2015.
 */
public class ListAdapter extends BaseExpandableListAdapter{

    private ArrayList<Player> mPlayers;
    private ArrayList<Team> mTeams;
    private int mGroupSize;

    private Context mContext;
    private IActivity mCallback;
    private LayoutInflater mInflater;

    private String playersTitleString;
    private String teamsTitleString;
    private String showMoreString;
    private static final int PLAYERS = 0;
    private static final int TEAMS = 1;

    public ListAdapter(Context context,ArrayList<Player> players,ArrayList<Team> teams)
    {
        mContext = context;
        mCallback = (IActivity) mContext;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupSize =2;
        mPlayers = players;
        if(!mPlayers.isEmpty()) mPlayers.add(new Player());
        mTeams = teams;
        if(!mTeams.isEmpty())mTeams.add(new Team());
        getStringResources();
    }

    private void getStringResources() {
        playersTitleString = mContext.getResources().getString(R.string.testString_Players);
        teamsTitleString = mContext.getResources().getString(R.string.testString_Teams);
        showMoreString = mContext.getResources().getString(R.string.show_more)+" ";
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.component_list_groupitem,
                null);
        TextView text = (TextView) view.findViewById(R.id.group_title);
        String title ="";
        if(groupPosition==PLAYERS)
            if(mPlayers.isEmpty()) view.setVisibility(View.INVISIBLE);
                else title = playersTitleString;
        else if(mTeams.isEmpty()) view.setVisibility(View.INVISIBLE);
                else title = teamsTitleString;
        text.setText(title);
        return view;
    }

    // ChildView is a custom build layout based on MainMenu layout
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView text = null;
          switch (groupPosition) {
            case PLAYERS:
                if ((childPosition != mPlayers.size()-1) && !(mPlayers.isEmpty())) {
                    view = mInflater.inflate(R.layout.component_list_players,
                            null);
                    view = populateViewPlayer(view, childPosition);
                } else if(!((mPlayers.size()-1)%10==0)) view = populateDummy(view,groupPosition);
                break;
            case TEAMS: {
                if((childPosition !=mTeams.size()-1) && !(mTeams.isEmpty())){
                    view = mInflater.inflate(R.layout.component_list_teams,
                        null);

                    view = populateViewTeam(view,childPosition);
                } else if(!((mTeams.size()-1)%10==0)) view = populateDummy(view,groupPosition);
            }
        }
        return view;
    }

    private View populateViewPlayer(View view,int position)    {
        TextView name_TV = (TextView) view.findViewById(R.id.item_name);
        TextView age_TV = (TextView) view.findViewById(R.id.item_age);
        TextView club_TV = (TextView) view.findViewById(R.id.item_club);
        ImageView imageView = (ImageView) view.findViewById(R.id.flag_view);

        name_TV.setText(mPlayers.get(position).getName());
        age_TV.setText(mPlayers.get(position).getAge()+"");
        club_TV.setText(mPlayers.get(position).getClub());
        FlagName.setDrawableToImageView(
                imageView,mContext,mPlayers.get(position).getNationality());
        return view;
    }

    private View populateViewTeam(View view,int position){
        TextView name_TV = (TextView) view.findViewById(R.id.item_name);
        TextView city_TV = (TextView) view.findViewById(R.id.item_city);
        TextView stadium_TV = (TextView) view.findViewById(R.id.item_stadium);
        ImageView imageView = (ImageView) view.findViewById(R.id.flag_view);

        name_TV.setText(mTeams.get(position).getName());
        city_TV.setText(mTeams.get(position).getCity());
        stadium_TV.setText(mTeams.get(position).getStadium());
        FlagName.setDrawableToImageView(
                imageView,mContext,mTeams.get(position).getNationality());
        return view;
    }

    private View populateDummy(View view,int position){
        view = mInflater.inflate(R.layout.component_list_showmore,null);
        TextView showMore = (TextView) view.findViewById(R.id.group_title);
        String showMoreStr = showMoreString;
        showMoreStr +=  (position==PLAYERS) ? playersTitleString : teamsTitleString;
        showMore.setText(showMoreStr);
        final int clickPosition = position;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            fetchMore(clickPosition);
            }
        });
        return view;
    }

    public void fetchMore(int position){
        if(position==PLAYERS) mCallback.fetchMore(PLAYERS,mPlayers.size());
        else mCallback.fetchMore(TEAMS,mTeams.size());
    }

    @Override
    public int getGroupCount() {
        return mGroupSize;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
       if(groupPosition==PLAYERS) return mPlayers.size();
        else return mTeams.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(groupPosition==PLAYERS) return mPlayers.get(childPosition);
        else return mTeams.get(childPosition);

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
