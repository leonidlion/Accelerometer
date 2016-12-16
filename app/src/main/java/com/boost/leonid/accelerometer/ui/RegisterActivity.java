package com.boost.leonid.accelerometer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    private static final int LAYOUT = R.layout.activity_sign_up;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @BindView(R.id.input_name)
    EditText mInputNameEdit;
    @BindView(R.id.input_email)
    EditText mInputEmailEdit;
    @BindView(R.id.input_password)
    EditText mInputPasswordEdit;
    @BindView(R.id.btn_signup)
    Button mSignupBtn;
    @BindView(R.id.link_login)
    TextView mSignInLabel;

    @OnClick({R.id.btn_signup, R.id.link_login})
    public void onClick(View view){
        Log.d(TAG, "onClick");
        switch (view.getId()){
            case R.id.btn_signup:
                signUp();
                break;
            case R.id.link_login:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    private void signUp(){
        if (!validate()){
            return;
        }
        mSignupBtn.setEnabled(false);
        showProgressDialog();

        final String name = mInputNameEdit.getText().toString();
        final String email = mInputEmailEdit.getText().toString();
        final String password = mInputPasswordEdit.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()){
                            onSignupSuccess(task.getResult().getUser(), name);
                        }else {
                            onSignupFailed();
                        }
                    }
                });
    }
    private void onSignupSuccess(FirebaseUser user, String name) {
        mSignupBtn.setEnabled(true);
        writeNewUser(user.getUid(), name, user.getEmail());
        setResult(RESULT_OK, null);
        finish();
    }

    private void writeNewUser(String uid, String name, String email) {
        User user = new User(name, email);
        mDatabase.child("users").child(uid).setValue(user);
    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), R.string.register_fail, Toast.LENGTH_LONG).show();
        mSignupBtn.setEnabled(true);
    }
    private boolean validate() {
        boolean valid = true;

        String name = mInputNameEdit.getText().toString();
        String email = mInputEmailEdit.getText().toString();
        String password = mInputPasswordEdit.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mInputNameEdit.setError(getString(R.string.name_input_error));
            valid = false;
        } else {
            mInputNameEdit.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mInputEmailEdit.setError(getString(R.string.email_input_error));
            valid = false;
        } else {
            mInputEmailEdit.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mInputPasswordEdit.setError(getString(R.string.pass_input_error));
            valid = false;
        } else {
            mInputPasswordEdit.setError(null);
        }

        return valid;
    }
}
