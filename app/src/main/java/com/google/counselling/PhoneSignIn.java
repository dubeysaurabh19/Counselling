package com.google.counselling;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneSignIn extends Activity{

    private static final String TAG = "com.google.com";

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private EditText phoneNumber, verificationCode;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_auth);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width) ,(int)(height*0.55));

        mAuth = FirebaseAuth.getInstance();
        phoneNumber = (EditText) findViewById(R.id.phone);
        verificationCode = (EditText) findViewById(R.id.code);

        Button send = (Button) findViewById(R.id.send_code_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhoneNumberVerification(phoneNumber.getText().toString());
            }
        });

        Button verify = (Button) findViewById(R.id.verification_button);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = verificationCode.getText().toString();
                verifyPhoneNumberWithCode(mVerificationId, code);

            }
        });

        Button resend = (Button) findViewById(R.id.resend_button);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend(phoneNumber.getText().toString(), mResendToken);
            }
        });

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
                Toast.makeText(PhoneSignIn.this, "Code send", Toast.LENGTH_LONG).show();
                mVerificationId = s;
                mResendToken = forceResendingToken;
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 2, TimeUnit.MINUTES, PhoneSignIn.this, mCallbacks);
        mVerificationInProgress = true;
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(PhoneSignIn.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCredential:success");
                    Toast.makeText(PhoneSignIn.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PhoneSignIn.this, Home.class);
                    startActivity(intent);
                    FirebaseUser user = task.getResult().getUser();
                    SharedPreferences sharedPreferences = PhoneSignIn.this.getSharedPreferences(SessionManager.PREFERENCES_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SessionManager.STATUS, true);
                    editor.putString(SessionManager.USER_ID, user.getPhoneNumber());
                    editor.apply();
                    addUserToDatabase(user);

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

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 2, TimeUnit.MINUTES, PhoneSignIn.this, mCallbacks, token);


    }

    public void onClick (View v) {
        resend(phoneNumber.getText().toString(), mResendToken);

    }

    public void addUserToDatabase(FirebaseUser firebaseUser) {

        User user = new User(firebaseUser.getUid(), firebaseUser.getPhoneNumber(), firebaseUser.getPhoneNumber());
        FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Successfully added user to database");

                        }else {
                            Log.w(TAG, "Error adding user to database");
                        }
                    }
                });



    }
}
