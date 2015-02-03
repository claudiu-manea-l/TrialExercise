package com.codezapps.trialexercise.UI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codezapps.trialexercise.R;

/**
 * Created by viperime on 2/3/2015.
 */
public class PagerFragment extends Fragment {

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    public static PagerFragment newInstance(){
        PagerFragment fragment = new PagerFragment();
        return fragment;
    }

    public ListFragment getFragment(int position){
        return mPagerAdapter.getFragment(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_pager, null);
        initializePager(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        // mSectionAdapter.stopLists();
        super.onDestroyView();
    }

    private void initializePager(View root) {
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) root.findViewById(R.id.pager);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        this.mPagerAdapter = new PagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
       // mViewPager.setOffscreenPageLimit(2);
    }
}
