package com.boost.leonid.accelerometer.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class LoginFragment extends BaseFragment {
    private static final String TAG = "LoginFragment";
    private static final int LAYOUT = R.layout.fragment_sign_in;
    private LoginActionListener mCallback;
    private FirebaseAuth mAuth;

    public interface LoginActionListener {
        void onCreateNewUserClick();
        void onSignInClick();
    }

    @BindView(R.id.et_input_email)
    EditText mInputEmailEdit;
    @BindView(R.id.et_input_password)
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
                mCallback.onCreateNewUserClick();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (LoginActionListener) context;
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
        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();

        return view;
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
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()){
                            mCallback.onSignInClick();
                        } else {
                            Toast.makeText(
                                    getContext(),
                                    R.string.signin_fail,
                                    Toast.LENGTH_SHORT).show();
                            mLoginBtn.setEnabled(true);
                        }
                    }
                });
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
}
