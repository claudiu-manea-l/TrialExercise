package com.codezapps.trialexercise.UI.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codezapps.trialexercise.R;

import com.codezapps.trialexercise.common.IActivity;
import com.codezapps.trialexercise.common.JSONHolder;
import com.codezapps.trialexercise.common.Worker;


public abstract class ListFragment extends Fragment implements ListView.OnItemClickListener, View.OnClickListener   {

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

    protected abstract String getName();
    protected abstract BaseAdapter initAdapter();
    protected abstract void handleInitialRequest(JSONHolder jsonHolder);
    protected abstract void handleSearchRequest(JSONHolder jsonHolder);
    protected abstract void showMore(JSONHolder jsonHolder);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.setEmptyView(view.findViewById(R.id.empty));
        View header = inflater.inflate(R.layout.component_list_groupitem,null);
        TextView title = (TextView) header.findViewById(R.id.group_title);
        title.setText(getName());
        mListView.addHeaderView(header);

        View footer = inflater.inflate(R.layout.component_list_showmore,null);
        TextView showMore = (TextView) footer.findViewById(R.id.group_title);
        showMore.setText("More "+getName());

        footer.setOnClickListener(this);
        mListView.addFooterView(footer);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
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

    public void handleWorkerPost(JSONHolder jsonHolder,int requestType)
    {
        switch(requestType) {
            case Worker.INITIAL_REQUEST:
                handleInitialRequest(jsonHolder);
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
    };


}
