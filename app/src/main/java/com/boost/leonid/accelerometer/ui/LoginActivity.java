package com.boost.leonid.accelerometer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.boost.leonid.accelerometer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity{
    private static final String TAG = "LoginActivity";
    private static final int LAYOUT = R.layout.activity_sign_in;
    private static final int REQUEST_SIGN_UP = 0;

    private FirebaseAuth mAuth;

    @BindView(R.id.input_email)
    EditText mInputEmailEdit;
    @BindView(R.id.input_password)
    EditText mInputPassEdit;
    @BindView(R.id.btn_login)
    Button mLoginBtn;
    @BindView(R.id.link_signup)
    TextView mSignInLink;

    @OnClick({R.id.btn_login, R.id.link_signup})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                signIn();
                break;
            case R.id.link_signup:
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGN_UP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            onAuthSuccess();
        }
    }

    private void signIn(){
        if (!validateForm()){
            return;
        }

        mLoginBtn.setEnabled(false);

        showProgressDialog();

        String email = mInputEmailEdit.getText().toString();
        String password = mInputPassEdit.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()){
                            onAuthSuccess();
                        } else {
                            Toast.makeText(
                                    LoginActivity.this,
                                    R.string.signin_fail,
                                    Toast.LENGTH_SHORT).show();
                            mLoginBtn.setEnabled(true);
                        }
                    }
                });
    }

    private void onAuthSuccess(){
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    private boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(mInputEmailEdit.getText().toString())){
            mInputEmailEdit.setError(getString(R.string.required));
            result = false;
        } else {
            mInputEmailEdit.setError(null);
        }

        if (TextUtils.isEmpty(mInputPassEdit.getText().toString())){
            mInputPassEdit.setError(getString(R.string.required));
            result = false;
        } else {
            mInputPassEdit.setError(null);
        }

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGN_UP){
            if (resultCode == RESULT_OK){
                onAuthSuccess();
            }
        }
    }
}
