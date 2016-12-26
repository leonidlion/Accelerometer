package com.boost.leonid.accelerometer.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boost.leonid.accelerometer.Const;
import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.adapter.ListDatesHolder;
import com.boost.leonid.accelerometer.model.AccelerometerData;
import com.boost.leonid.accelerometer.model.HistoryItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import java.util.List;

public class SessionListFragment extends BaseFragment {
    private static final String TAG = "SessionListFragment";
    private static final int LAYOUT = R.layout.fragment_list_dates;
    private FirebaseRecyclerAdapter<HistoryItem, ListDatesHolder> mAdapter;
    private RecyclerView mRecycler;
    private SessionListInteractionListener mCallback;

    public interface SessionListInteractionListener{
        void onSessionItemClick(List<AccelerometerData> model);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (SessionListInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.list_dates_recycler);
        mRecycler.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = getQuery();
        Log.d(TAG, query.toString());
        mAdapter = new FirebaseRecyclerAdapter<HistoryItem, ListDatesHolder>(HistoryItem.class, R.layout.list_dates_item, ListDatesHolder.class, query) {
            @Override
            protected void populateViewHolder(ListDatesHolder viewHolder, final HistoryItem model, int position) {

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallback.onSessionItemClick(model.getCoordinates());
                    }
                });
                viewHolder.bind(model);
            }
        };

        mRecycler.setAdapter(mAdapter);
    }

    private Query getQuery() {
        return Const.getDataCoordinatesOfUserReference(getUid());
    }
}
