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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class BlogList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String category;
    TextView awareness, burning, counsel, human, love, laughter, personality, psychology, uncategorized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Typeface aceme = Typeface.createFromAsset(getAssets(), "fonts/Acme-Regular.ttf");

        awareness = (TextView) findViewById(R.id.awareness);
        burning = (TextView) findViewById(R.id.burningIssues);
        counsel = (TextView) findViewById(R.id.counsel);
        human = (TextView) findViewById(R.id.human);
        love = (TextView) findViewById(R.id.love);
        laughter = (TextView) findViewById(R.id.laughter);
        personality = (TextView) findViewById(R.id.personality);
        psychology = (TextView) findViewById(R.id.psychology);
        uncategorized = (TextView) findViewById(R.id.uncategorized);

        awareness.setTypeface(aceme);
        burning.setTypeface(aceme);
        counsel.setTypeface(aceme);
        human.setTypeface(aceme);
        laughter.setTypeface(aceme);
        love.setTypeface(aceme);
        personality.setTypeface(aceme);
        psychology.setTypeface(aceme);
        uncategorized.setTypeface(aceme);

        awareness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Awareness";
                Intent intent = new Intent(BlogList.this, Blogs.class);
                intent.putExtra("Category", category);
                startActivity(intent);
            }
        });

        burning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Burning Issues";
                Intent intent = new Intent(BlogList.this, Blogs.class);
                intent.putExtra("Category", category);
                startActivity(intent);
            }
        });

        counsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Counsel and Cure";
                Intent intent = new Intent(BlogList.this, Blogs.class);
                intent.putExtra("Category", category);
                startActivity(intent);
            }
        });

        human.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Human Nature";
                Intent intent = new Intent(BlogList.this, Blogs.class);
                intent.putExtra("Category", category);
                startActivity(intent);
            }
        });

        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Love";
                Intent intent = new Intent(BlogList.this, Blogs.class);
                intent.putExtra("Category", category);
                startActivity(intent);
            }
        });

        laughter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Laughter";
                Intent intent = new Intent(BlogList.this, Blogs.class);
                intent.putExtra("Category", category);
                startActivity(intent);
            }
        });

        personality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Personality";
                Intent intent = new Intent(BlogList.this, Blogs.class);
                intent.putExtra("Category", category);
                startActivity(intent);
            }
        });

        psychology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Psychology";
                Intent intent = new Intent(BlogList.this, Blogs.class);
                intent.putExtra("Category", category);
                startActivity(intent);
            }
        });

        uncategorized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Uncategorized";
                Intent intent = new Intent(BlogList.this, Blogs.class);
                intent.putExtra("Category", category);
                startActivity(intent);
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
        getMenuInflater().inflate(R.menu.blog_list, menu);
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
            Toast.makeText(BlogList.this, "Signed out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BlogList.this, LoginScreen.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(BlogList.this, Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_blogs) {

        } else if (id == R.id.nav_watch) {
            Intent intent = new Intent(BlogList.this, Watch.class);
            startActivity(intent);
        } else if (id == R.id.nav_mood) {
            Intent intent = new Intent(BlogList.this, Mood.class);
            startActivity(intent);
        } else if (id == R.id.nav_counsellor) {
            Intent intent = new Intent(BlogList.this, Counsellor.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Download this awesome app. http://www.google.co.in");
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent,"Share Application" ));
        } else if (id == R.id.nav_connect) {
            Intent intent = new Intent(BlogList.this, Connect.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
