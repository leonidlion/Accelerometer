package com.boost.leonid.accelerometer.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.model.Coordinates;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private FirebaseDatabase mFirebaseDatabase;

    @BindView(R.id.hello)
    TextView mHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        ButterKnife.bind(this);
        List<Double> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++){
            list.add(random.nextDouble());
        }
        Coordinates coordinates = new Coordinates("13.12","14:00", list, list, list);

        mFirebaseDatabase.getReference().child("users").child(getUid()).child("coord").setValue(coordinates);

        mHello.setText("Hello " + FirebaseAuth.getInstance().getCurrentUser().getEmail());


    }

}
