package com.google.counselling;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends Fragment {

    private static final String TAG = "Register";
    boolean state;

    private EditText emailInput, userName;
    private EditText passwordInput;

    FirebaseUser user;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        userName = (EditText) view.findViewById(R.id.username);
        emailInput = (EditText) view.findViewById(R.id.email);
        passwordInput = (EditText) view.findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        //user = mAuth.getCurrentUser();
        //addUserToDatabase(user);
        Button registerButton = (Button) view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = connectivity();
                if(state){
                    createAccount(emailInput.getText().toString(), passwordInput.getText().toString());
                }
                else {
                    Toast.makeText(getActivity(), "Not connected to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailInput.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Required.");
            valid = false;
        } else {
            emailInput.setError(null);
        }

        String password = passwordInput.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Required.");
            valid = false;
        } else {
            passwordInput.setError(null);
        }

        if (password.length()<6){
            passwordInput.setError("Atleast 6 characters long");
            valid = false;
        }

        return valid;
    }

    private void createAccount (String email, String password) {
        Log.d(TAG, "createdAccount: "+email);
        if (!validateForm()){
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "createUserWithEmail:success");
                    Toast.makeText(getActivity(), "Account Created",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    addUserToDatabase(user);
                    startActivity(new Intent(getActivity(), LoginScreen.class));
                    //updateUI(user);
                }
                else{
                    Log.w(TAG, "createUserWithEmail:failure");
                    Toast.makeText(getActivity(),"Email already exists",Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });
    }

    public void addUserToDatabase(FirebaseUser firebaseUser) {

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(userName.getText().toString()).build();
        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "Display Name set");
                }
            }
        });

        User user = new User(firebaseUser.getUid(),firebaseUser.getEmail(),/* FirebaseInstanceId.getInstance().getToken(),*/ userName.getText().toString() );

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

    boolean connectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

}