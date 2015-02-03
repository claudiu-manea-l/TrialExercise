package com.codezapps.trialexercise.UI.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codezapps.trialexercise.R;
import com.codezapps.trialexercise.common.FlagName;
import com.codezapps.trialexercise.common.IActivity;
import com.codezapps.trialexercise.model.Player;
import com.codezapps.trialexercise.model.Team;

import java.util.ArrayList;

/**
 * Created by viperime on 2/3/2015.
 */
public class PlayersAdapter extends BaseAdapter{
    public static final int PLAYERS = 0;

    private ViewHolder sHolder;
    private ArrayList<Player> mPlayers;
    private Context mContext;
    private IActivity mCallback;
    private LayoutInflater mInflater;

    public PlayersAdapter(Context context,ArrayList<Player> players)
    {
        mContext = context;
        mCallback = (IActivity) context;
        mPlayers = players;
        if(!mPlayers.isEmpty()) mPlayers.add(new Player());
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent){
        Player currPlayer = mPlayers.get(position);

        View view = convertView;
        if((position!=mPlayers.size()-1) && !(mPlayers.isEmpty())) {
            if (convertView == null) {
                view = mInflater.inflate(R.layout.component_list_players, null);
                sHolder = new ViewHolder();
                sHolder.tv_name = (TextView) view.findViewById(R.id.item_name);
                sHolder.tv_age = (TextView) view.findViewById(R.id.item_age);
                sHolder.tv_club = (TextView) view.findViewById(R.id.item_club);
                sHolder.iv_flag = (ImageView) view.findViewById(R.id.flag_view);
                view.setTag(sHolder);
            } else {
                sHolder = (ViewHolder) view.getTag();
            }
            sHolder.tv_name.setText(currPlayer.getName());
          //  sHolder.tv_age.setText(currPlayer.getAge());
           // sHolder.tv_club.setText(currPlayer.getClub());
            FlagName.setDrawableToImageView(
                    sHolder.iv_flag, mContext, currPlayer.getNationality());
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
                mCallback.fetchMore(PLAYERS,clickPosition);
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return mPlayers.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlayers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        TextView tv_name,tv_age,tv_club;
        ImageView iv_flag;
    }
}
