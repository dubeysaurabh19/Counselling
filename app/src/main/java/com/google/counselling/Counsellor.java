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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Counsellor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Counsellor";

    CounsellorModel counsellor;
    Button send;
    EditText name, email, address, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        name = (EditText) findViewById(R.id.Name);
        email = (EditText) findViewById(R.id.Email);
        address = (EditText) findViewById(R.id.Address);
        phone = (EditText) findViewById(R.id.Phone);

        send = (Button) findViewById(R.id.submitButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counsellor = new CounsellorModel(name.getText().toString(), address.getText().toString(), email.getText().toString(), phone.getText().toString());
                name.setText("");
                email.setText("");
                phone.setText("");
                address.setText("");
                addCounsellor();
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
        getMenuInflater().inflate(R.menu.counsellor, menu);
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
            Toast.makeText(Counsellor.this, "Signed out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Counsellor.this, LoginScreen.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(Counsellor.this, Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_blogs) {
            Intent intent = new Intent(Counsellor.this, BlogList.class);
            startActivity(intent);
        } else if (id == R.id.nav_watch) {
            Intent intent = new Intent(Counsellor.this, Watch.class);
            startActivity(intent);
        } else if (id == R.id.nav_mood) {
            Intent intent = new Intent(Counsellor.this, Mood.class);
            startActivity(intent);
        } else if (id == R.id.nav_counsellor) {

        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Download this awesome app. http://www.google.co.in");
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent,"Share Application" ));
        } else if (id == R.id.nav_connect) {
            Intent intent = new Intent(Counsellor.this, Connect.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void addCounsellor () {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("/counsellors/").child(counsellor.getName()).push();
        reference.setValue(counsellor);
        //Log.d(TAG, "Added Counsellor to database");
        Toast.makeText(Counsellor.this, "Details submitted", Toast.LENGTH_SHORT).show();

    }
}
