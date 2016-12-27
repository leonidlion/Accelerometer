package com.boost.leonid.accelerometer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.adapter.SessionDetailAdapter;
import com.boost.leonid.accelerometer.model.AccelerometerData;

import java.util.List;


public class SessionDetailFragment extends Fragment {
    private static final String TAG = "SessionDetailFragment";
    private static final int LAYOUT = R.layout.fragment_detail_list;
    private List<AccelerometerData> mAccelerometerDataList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccelerometerDataList = getArguments().getParcelableArrayList(TabFragment.ARGS_ACCELEROMETER_DATA_LIST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);

        SessionDetailAdapter adapter = new SessionDetailAdapter(getContext(), mAccelerometerDataList);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);

        return view;
    }
}
