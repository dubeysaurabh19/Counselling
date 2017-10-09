package com.google.counselling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MoodDisplay extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        heading = intent.getStringExtra("Heading");

        TextView headingText = (TextView) findViewById(R.id.heading);
        TextView contentText = (TextView) findViewById(R.id.content);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Acme-Regular.ttf");
        headingText.setTypeface(typeface);
        contentText.setTypeface(typeface);

        if (heading.equals("Angry")){
            headingText.setText(getResources().getString(R.string.angry));
            //headingText.setBackgroundColor(getResources().getColor(R.color.Angry));
            contentText.setText(getResources().getString(R.string.angry_content));
        }
        else if(heading.equals("Confused")) {
            headingText.setText(getResources().getString(R.string.confused));
            //headingText.setBackgroundColor(getResources().getColor(R.color.Confused));
            contentText.setText(getResources().getString(R.string.confused_content));
        }
        else if(heading.equals("Dont Know")) {
            headingText.setText(getResources().getString(R.string.dont));
            //headingText.setBackgroundColor(getResources().getColor(R.color.Dont));
            contentText.setText(getResources().getString(R.string.dont_content));
        }
        else if(heading.equals("Goofy")) {
            headingText.setText(getResources().getString(R.string.goofy));
            //headingText.setBackgroundColor(getResources().getColor(R.color.Goofy));
            contentText.setText(getResources().getString(R.string.goofy_content));
        }
        else if(heading.equals("Happy")) {
            headingText.setText(getResources().getString(R.string.happy));
            //headingText.setBackgroundColor(getResources().getColor(R.color.Happy));
            contentText.setText(getResources().getString(R.string.happy_content));
        }
        else if(heading.equals("Heartbroken")) {
            headingText.setText(getResources().getString(R.string.heartbroken));
            //headingText.setBackgroundColor(getResources().getColor(R.color.Heartbroken));
            contentText.setText(getResources().getString(R.string.heartbroken_content));
        }
        else if(heading.equals("Hurt")) {
            headingText.setText(getResources().getString(R.string.hurt));
            headingText.setBackgroundColor(getResources().getColor(R.color.Hurt));
            contentText.setText(getResources().getString(R.string.hurt_content));
        }
        else if(heading.equals("Loved")) {
            headingText.setText(getResources().getString(R.string.love));
            //headingText.setBackgroundColor(getResources().getColor(R.color.Loved));
            contentText.setText(getResources().getString(R.string.love_content));
        }
        else if(heading.equals("Sad")) {
            headingText.setText(getResources().getString(R.string.sad));
            //headingText.setBackgroundColor(getResources().getColor(R.color.Sad));
            contentText.setText(getResources().getString(R.string.sad_content));
        }
        else if(heading.equals("Sexy")) {
            headingText.setText(getResources().getString(R.string.sexy));
            //headingText.setBackgroundColor(getResources().getColor(R.color.Sexy));
            contentText.setText(getResources().getString(R.string.sexy_content));
        }
        else if(heading.equals("Shattered")) {
            headingText.setText(getResources().getString(R.string.shattered));
            //headingText.setBackgroundColor(getResources().getColor(R.color.Shattered));
            contentText.setText(getResources().getString(R.string.shattered_content));
        }
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
        getMenuInflater().inflate(R.menu.mood_display, menu);
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
            Toast.makeText(MoodDisplay.this, "Signed out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MoodDisplay.this, LoginScreen.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(MoodDisplay.this, Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_blogs) {
            Intent intent = new Intent(MoodDisplay.this, BlogList.class);
            startActivity(intent);
        } else if (id == R.id.nav_watch) {
            Intent intent = new Intent(MoodDisplay.this, Watch.class);
            startActivity(intent);
        } else if (id == R.id.nav_mood) {
            Intent intent = new Intent(MoodDisplay.this, Mood.class);
            startActivity(intent);
        } else if (id == R.id.nav_counsellor) {
            Intent intent = new Intent(MoodDisplay.this, Counsellor.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Download this awesome app. http://www.google.co.in");
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent,"Share Application" ));
        } else if (id == R.id.nav_connect) {
            Intent intent = new Intent(MoodDisplay.this, Connect.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
