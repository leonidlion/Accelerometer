package com.boost.leonid.accelerometer.ui.fragment;

import android.content.Context;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.adapter.ListDatesHolder;
import com.boost.leonid.accelerometer.model.Coordinates;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ListDatesFragment extends Fragment {
    private static final String TAG = "ListDatesFragment";
    private static final int LAYOUT = R.layout.fragment_list_dates;
    private FirebaseRecyclerAdapter<Coordinates, ListDatesHolder> mAdapter;
    private RecyclerView mRecycler;
    private DatabaseReference mDatabase;
    private ClickItemListener mCallback;

    public interface ClickItemListener{
        void onItemClick(Coordinates refKey);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (ClickItemListener) context;
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

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) view.findViewById(R.id.list_dates_recycler);
        mRecycler.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = getQuery(mDatabase);
        Log.d(TAG, query.toString());
        mAdapter = new FirebaseRecyclerAdapter<Coordinates, ListDatesHolder>(Coordinates.class, R.layout.list_dates_item, ListDatesHolder.class, query) {
            @Override
            protected void populateViewHolder(ListDatesHolder viewHolder, final Coordinates model, int position) {
                final DatabaseReference ref = getRef(position);
                final String refKey = ref.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "click " + refKey);
                        mCallback.onItemClick(model);
                    }
                });
                viewHolder.bind(model);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    private Query getQuery(DatabaseReference database) {
        return database.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("acc_data");
    }
}
