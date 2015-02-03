package com.codezapps.trialexercise.UI.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codezapps.trialexercise.model.Player;

import java.util.ArrayList;

/**
 * Created by viperime on 2/3/2015.
 */
public class PagerAdapter extends FragmentPagerAdapter{

    private ArrayList<ListFragment> mPagerFragments;
    private FragmentManager mFragmentManager;
    private String[] titles = {"Players","Teams"};;

    public PagerAdapter(FragmentManager fm)
    {
        super(fm);
        mFragmentManager = fm;
        mPagerFragments = new ArrayList<ListFragment>();
        for (int i = 0; i < 2; i++)
            mPagerFragments.add(null);
    }

    public ListFragment getFragment(int position)
    {
        return mPagerFragments.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        if (mPagerFragments.get(position) == null ) {
            ListFragment fragment;
            if(position==PlayersAdapter.PLAYERS)
                fragment =PlayersFragment.newInstance();
               else
                fragment =TeamsFragment.newInstance();
            mPagerFragments.set(position,fragment);
        }
        return mPagerFragments.get(position);
    }

    /**
     * returns to the ViewPager the amount of pages the Section contains
     */
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public String getPageTitle(int position) {
        return titles[position];
    }
}
