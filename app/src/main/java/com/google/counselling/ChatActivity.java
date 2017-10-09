package com.google.counselling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Chat";

    String username;
    EditText messageText;
    ImageButton sendButton;
    LinearLayout layout;
    ScrollView scrollView;
    User user, newUser;
    ChatMessageModel chatMessage;
    ChatModel chatModel;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    TextView messageTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        messageText = (EditText) findViewById(R.id.userMessage);
        sendButton = (ImageButton) findViewById(R.id.sendButton);
        layout = (LinearLayout) findViewById(R.id.linearLayout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        user = new User(firebaseUser.getUid(),firebaseUser.getEmail(), /*FirebaseInstanceId.getInstance().getToken(),*/ firebaseUser.getDisplayName());

        //Toast.makeText(ChatActivity.this,"onCreate " + firebaseUser.getDisplayName() , Toast.LENGTH_SHORT).show();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessageToDatabase(messageText.getText().toString());
                messageText.setText("");
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference reference = database.getReference("/messages/" + user.uid + "_W5I30G3puKcGI5CwkhMmVPMQWJh1");
        DatabaseReference reference = database.getReference("/messages/" + user.userName);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getRef().getKey();
                //Log.d(TAG, key);
                chatModel = dataSnapshot.getValue(ChatModel.class);
                //Log.d(TAG, chatModel.getMessage());
                if (chatModel.getuId().equals(firebaseUser.getUid())) {
                    Log.d(TAG, chatModel.getMessage());
                    addMessage("You:\n" + chatModel.getMessage(), 1);
                }
                else if(chatModel.getuId().equals("W5I30G3puKcGI5CwkhMmVPMQWJh1")) {
                    Log.d(TAG, chatModel.getMessage());
                    addMessage("Counsellor:\n" + chatModel.getMessage(), 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sharedPreferences = getSharedPreferences(SessionManager.PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SessionManager.STATUS, false);
            editor.putString(SessionManager.USER_ID, "");
            editor.apply();
            Toast.makeText(ChatActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChatActivity.this, LoginScreen.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(ChatActivity.this, Home.class);
            startActivity(intent);        } else if (id == R.id.nav_blogs) {
            Intent intent = new Intent(ChatActivity.this, BlogList.class);
            startActivity(intent);
        } else if (id == R.id.nav_watch) {
            Intent intent = new Intent(ChatActivity.this, Watch.class);
            startActivity(intent);
        } else if (id == R.id.nav_mood) {
            Intent intent = new Intent(ChatActivity.this, Mood.class);
            startActivity(intent);
        } else if (id == R.id.nav_counsellor) {
            Intent intent = new Intent(ChatActivity.this, Counsellor.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Download this awesome app. http://www.google.co.in");
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent,"Share Application" ));
        } else if (id == R.id.nav_connect) {
            Intent intent = new Intent(ChatActivity.this, Connect.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addMessage(String message, int type) {

        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 20);
        //textView.setGravity(5);
        textView.setLayoutParams(lp);
        //messageTextView.setText(message);
        if (type == 1) {
            textView.setBackgroundColor(getResources().getColor(R.color.background_color));
            textView.setTextColor(getResources().getColor(R.color.white));
            //textView.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else {
            textView.setBackgroundColor(getResources().getColor(R.color.background_color));
            textView.setTextColor(getResources().getColor(R.color.white));
            //textView.setBackgroundResource(R.drawable.rounded_corner2);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }

    void addMessageToDatabase(String message) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference reference = database.getReference("/messages/" + user.uid + "_W5I30G3puKcGI5CwkhMmVPMQWJh1");
        DatabaseReference reference = database.getReference("/messages/" + user.userName);
        DatabaseReference messageReference = reference.push();
        messageReference.setValue(new ChatModel(user.uid, message));

    }

}
