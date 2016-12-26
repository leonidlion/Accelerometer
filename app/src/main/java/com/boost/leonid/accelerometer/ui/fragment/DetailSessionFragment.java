package com.boost.leonid.accelerometer.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boost.leonid.accelerometer.Const;
import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.adapter.ListDatesHolder;
import com.boost.leonid.accelerometer.model.HistoryItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class DetailSessionFragment extends Fragment {
    private static final String TAG = "DetailSessionFragment";
    private static final int LAYOUT = R.layout.fragment_list_dates;
    private FirebaseRecyclerAdapter<HistoryItem, ListDatesHolder> mAdapter;
    private RecyclerView mRecycler;
//    private ClickItemListener mCallback;

/*    public interface ClickItemListener{
        void onItemClick(HistoryItem refKey);
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mCallback = (ClickItemListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mCallback = null;
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
                final DatabaseReference ref = getRef(position);
                final String refKey = ref.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "click " + refKey);
//                        mCallback.onItemClick(model);
                    }
                });
                viewHolder.bind(model);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    private Query getQuery() {
        return Const.getDataCoordinatesOfUserReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
}
