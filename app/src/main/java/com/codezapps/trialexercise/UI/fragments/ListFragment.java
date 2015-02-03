package com.codezapps.trialexercise.UI.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codezapps.trialexercise.R;

import com.codezapps.trialexercise.common.IActivity;
import com.codezapps.trialexercise.common.JSONHolder;
import com.codezapps.trialexercise.common.Worker;


public abstract class ListFragment extends Fragment implements
        ListView.OnItemClickListener {

    //protected abstract BaseAdapter initAdapter();

    protected IActivity mCallback;
    /**
     * The fragment's ListView/GridView.
     */
    protected ListView mListView;

    /**
     * The Adapter which will be used to populate the ListView with
     * Views.
     */
    protected BaseAdapter mAdapter;
    protected ProgressBar mProgressBar;
    protected View mFooterView;

    protected abstract String getName();
    protected abstract BaseAdapter initAdapter();
    protected abstract void handleInitialRequest(JSONHolder jsonHolder);
    protected abstract void handleSearchRequest(JSONHolder jsonHolder);
    protected abstract void showMore(JSONHolder jsonHolder,int position);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.setEmptyView(view.findViewById(R.id.empty));
        mListView.setOnItemClickListener(this);
        mFooterView = inflater.inflate(R.layout.component_list_showmore,null);
        TextView showMore = (TextView) mFooterView.findViewById(R.id.group_title);
        showMore.setText("More "+getName());

        mListView.addFooterView(mFooterView,null,true);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);

        // Set OnItemClickListener so we can be notified on item clicks

        mAdapter = initAdapter();
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (IActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public void notifyDataChanged()
    {
        if(mAdapter!=null) mAdapter.notifyDataSetChanged();
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void handleWorkerPost(JSONHolder jsonHolder,int requestType,int position)
    {
        switch(requestType) {
            case Worker.INITIAL_REQUEST:
                handleInitialRequest(jsonHolder);
                break;
            case Worker.SEARCH_REQUEST:
                handleSearchRequest(jsonHolder);
                break;
            case Worker.SHOWMORE_REQUEST:
                showMore(jsonHolder,position);
                break;
            default:
                break;
        }
        mProgressBar.setVisibility(View.INVISIBLE);
        mAdapter.notifyDataSetChanged();
    };

}
