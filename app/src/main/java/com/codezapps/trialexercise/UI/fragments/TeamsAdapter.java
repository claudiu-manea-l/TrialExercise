package com.codezapps.trialexercise.UI.fragments;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codezapps.trialexercise.R;
import com.codezapps.trialexercise.common.FlagName;
import com.codezapps.trialexercise.common.IActivity;
import com.codezapps.trialexercise.model.Team;

import java.util.ArrayList;

/**
 * Created by viperime on 2/3/2015.
 */
public class TeamsAdapter extends BaseAdapter{

    public static final int TEAMS = 1;

    private ViewHolder sHolder;
    private ArrayList<Team> mTeams;
    private Context mContext;
    private IActivity mCallback;
    private LayoutInflater mInflater;

    public TeamsAdapter(Context context,ArrayList<Team> teams)
    {
        mContext = context;
        mCallback = (IActivity) context;
        mTeams = teams;
        if(!mTeams.isEmpty()) mTeams.add(new Team());
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent){
        Team currTeam = mTeams.get(position);

        View view = convertView;
        if((position!=mTeams.size()-1) && !(mTeams.isEmpty())) {
            if (convertView == null) {
                view = mInflater.inflate(R.layout.component_list_teams, null);
                sHolder = new ViewHolder();
                sHolder.tv_name = (TextView) view.findViewById(R.id.item_name);
                sHolder.tv_city = (TextView) view.findViewById(R.id.item_city);
                sHolder.tv_stadium = (TextView) view.findViewById(R.id.item_stadium);
                sHolder.iv_flag = (ImageView) view.findViewById(R.id.flag_view);
                view.setTag(sHolder);
            } else {
                sHolder = (ViewHolder) view.getTag();
            }
            sHolder.tv_name.setText(mTeams.get(position).getName());
            sHolder.tv_city.setText(mTeams.get(position).getCity());
            sHolder.tv_stadium.setText(mTeams.get(position).getStadium());
            FlagName.setDrawableToImageView(
                    sHolder.iv_flag, mContext, mTeams.get(position).getNationality());
    } else populateDummy(view,position);
        return view;
    }

    private View populateDummy(View view,int position){
        view = mInflater.inflate(R.layout.component_list_showmore,null);
        TextView showMore = (TextView) view.findViewById(R.id.group_title);

        showMore.setText("More Players");
        final int clickPosition = position;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.fetchMore(TEAMS,clickPosition);
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return mTeams.size();
    }

    @Override
    public Object getItem(int position) {
        return mTeams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        TextView tv_name,tv_city,tv_stadium;
        ImageView iv_flag;
    }
}
