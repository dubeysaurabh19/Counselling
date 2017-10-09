package com.google.counselling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends Fragment {

    private static final String TAG = "SignIn";
    private boolean LoggedIn = false;
    boolean state;


    private EditText emailInput;
    private EditText passwordInput;

    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        emailInput = (EditText) view.findViewById(R.id.email);
        //emailInput.setHintTextColor(getResources().getColor(R.color.hint_color));
        passwordInput = (EditText) view.findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        Button signInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = connectivity();
                if(state) {
                    signIn(emailInput.getText().toString(), passwordInput.getText().toString());
                }
                else {
                    Toast.makeText(getActivity(), "Not connected to internet", Toast.LENGTH_SHORT);
                }
            }
        });

        TextView phone = (TextView) view.findViewById(R.id.phoneTextView);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PhoneSignIn.class));
            }
        });

        /*Button signOutButton = (Button) view.findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SessionManager.PREFERENCES_NAME, Context.MODE_PRIVATE);
        LoggedIn = sharedPreferences.getBoolean(SessionManager.STATUS, false);
        if(LoggedIn){
            Intent intent = new Intent(getActivity(), Home.class);
            startActivity(intent);
        }
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

        return valid;
    }


    private void signIn (final String email, String password) {
        Log.d(TAG, "signIn: "+email);
        if (!validateForm()){
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail:success");
                    Toast.makeText(getActivity(), "Signed in",Toast.LENGTH_SHORT).show();
                    FirebaseUser user =  mAuth.getCurrentUser();
                    updateUI(user);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SessionManager.PREFERENCES_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SessionManager.STATUS, true);
                    editor.putString(SessionManager.USER_ID, email);
                    editor.apply();
                    //addUserToDatabase(user);
                    Intent intent = new Intent(getActivity(), Home.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Log.w(TAG, "signInWithEmail:failure");
                    Toast.makeText(getActivity(),"Wrong password",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

            }
        });
    }


    /*private void signOut(){
        mAuth.signOut();
        updateUI(null);
        Toast.makeText(getActivity(), "Signed out", Toast.LENGTH_SHORT).show();
    }*/

    private void updateUI (FirebaseUser user) {
        if(user!=null){

            getView().findViewById(R.id.email_login_form).setVisibility(View.GONE);
            //getView().findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_button).setVisibility(View.GONE);

            //findViewById(R.id.email_login_form).setVisibility(View.GONE);
        }
        else {
            getView().findViewById(R.id.email_login_form).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

            //getView().findViewById(R.id.sign_out_button).setVisibility(View.GONE);


        }
    }

    /*public void addUserToDatabase(FirebaseUser firebaseUser) {
        User user = new User(firebaseUser.getUid(),firebaseUser.getEmail(), FirebaseInstanceId.getInstance().getToken(), );

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

    }*/

    boolean connectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

}
