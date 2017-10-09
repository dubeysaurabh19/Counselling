package com.google.counselling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

public class Phone extends Fragment{

    private static final String TAG = "com.google.com";

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private EditText phoneNumber, verificationCode;
    FirebaseAuth mAuth;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phone_auth, container, false);

        mAuth = FirebaseAuth.getInstance();
        phoneNumber = (EditText) view.findViewById(R.id.phone);
        verificationCode = (EditText) view.findViewById(R.id.code);

        Button send = (Button) view.findViewById(R.id.send_code_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhoneNumberVerification(phoneNumber.getText().toString());
            }
        });

        Button verify = (Button) view.findViewById(R.id.verification_button);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = verificationCode.getText().toString();
                verifyPhoneNumberWithCode(mVerificationId, code);

            }
        });

        Button resend = (Button) view.findViewById(R.id.resend_button);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend(phoneNumber.getText().toString(), mResendToken);
            }
        });

        /*Button signOut = (Button) view.findViewById(R.id.sign_out_button);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });*/

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                mVerificationInProgress = false;

                //signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    phoneNumber.setError("Invalid phone number");
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d(TAG, "onCodeSent: " + s);
                Toast.makeText(getActivity(), "Code send", Toast.LENGTH_LONG).show();
                mVerificationId = s;
                mResendToken = forceResendingToken;
            }
        };

        return view;
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 2, TimeUnit.MINUTES, getActivity(), mCallbacks);
        mVerificationInProgress = true;
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCredential:success");
                    Toast.makeText(getActivity(), "Sign in successfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Home.class);
                    startActivity(intent);
                    FirebaseUser user = task.getResult().getUser();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SessionManager.PREFERENCES_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SessionManager.STATUS, true);
                    editor.putString(SessionManager.USER_ID, user.getPhoneNumber());
                    editor.apply();

                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        verificationCode.setError("Invalid code");
                    }
                }
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void resend(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 2, TimeUnit.MINUTES, getActivity(), mCallbacks, token);


    }

    public void onClick (View v) {
        resend(phoneNumber.getText().toString(), mResendToken);

    }

}
