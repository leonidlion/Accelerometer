package com.boost.leonid.accelerometer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.ui.fragment.LoginFragment;
import com.boost.leonid.accelerometer.ui.fragment.RegisterFragment;
import com.google.firebase.auth.FirebaseAuth;

public class AuthorizeActivity extends AppCompatActivity implements LoginFragment.LoginActionListener, RegisterFragment.RegisterActionListener {
    private static final String TAG = "AuthorizeActivity";
    private static final int FRAME_CONTAINER = R.id.authorize_frame_container;
    private static final int LAYOUT = R.layout.activity_authorize;

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            onAuthSuccess();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mAuth = FirebaseAuth.getInstance();
        getSupportFragmentManager().beginTransaction()
                .add(FRAME_CONTAINER, new LoginFragment())
                .commit();
    }

    @Override
    public void onCreateNewUserClick() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out)
                .replace(FRAME_CONTAINER, new RegisterFragment())
                .commit();
    }

    @Override
    public void onSignInClick() {
        onAuthSuccess();
    }

    @Override
    public void onAlreadyMemberClick() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out)
                .replace(FRAME_CONTAINER, new LoginFragment())
                .commit();
    }

    @Override
    public void onSignUpClick() {
        onAuthSuccess();
    }

    private void onAuthSuccess(){
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
