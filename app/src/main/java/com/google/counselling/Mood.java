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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Mood extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView moodListView;
    String[] moods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView angry = (TextView) findViewById(R.id.angry);
        angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mood.this, MoodDisplay.class);
                intent.putExtra("Heading", "Angry");
                startActivity(intent);
            }
        });

        TextView confused = (TextView) findViewById(R.id.confused);
        confused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mood.this, MoodDisplay.class);
                intent.putExtra("Heading", "Confused");
                startActivity(intent);
            }
        });

        TextView dont = (TextView) findViewById(R.id.dont);
        dont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mood.this, MoodDisplay.class);
                intent.putExtra("Heading", "Dont Know");
                startActivity(intent);
            }
        });

        TextView goofy = (TextView) findViewById(R.id.goofy);
        goofy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mood.this, MoodDisplay.class);
                intent.putExtra("Heading", "Goofy");
                startActivity(intent);
            }
        });

        TextView happy = (TextView) findViewById(R.id.happy);
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mood.this, MoodDisplay.class);
                intent.putExtra("Heading", "Happy");
                startActivity(intent);
            }
        });

        TextView heart = (TextView) findViewById(R.id.heart);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mood.this, MoodDisplay.class);
                intent.putExtra("Heading", "Heartbroken");
                startActivity(intent);
            }
        });

        TextView hurt = (TextView) findViewById(R.id.hurt);
        hurt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mood.this, MoodDisplay.class);
                intent.putExtra("Heading", "Hurt");
                startActivity(intent);
            }
        });

        TextView love = (TextView) findViewById(R.id.love);
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mood.this, MoodDisplay.class);
                intent.putExtra("Heading", "Loved");
                startActivity(intent);
            }
        });

        TextView sad = (TextView) findViewById(R.id.sad);
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mood.this, MoodDisplay.class);
                intent.putExtra("Heading", "Sad");
                startActivity(intent);
            }
        });

        TextView sexy = (TextView) findViewById(R.id.sexy);
        sexy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mood.this, MoodDisplay.class);
                intent.putExtra("Heading", "Sexy");
                startActivity(intent);
            }
        });

        TextView shattered = (TextView) findViewById(R.id.shattered);
        shattered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mood.this, MoodDisplay.class);
                intent.putExtra("Heading", "Shattered");
                startActivity(intent);
            }
        });



        /*moodListView = (ListView) findViewById(R.id.moodListView);
        moods = new String[] {"Happy", "Sad", "Angry", "Gloomy", "Lazy"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, moods){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);
                if(position%2 == 1) {
                    view.setBackgroundColor(Color.parseColor("#EC407A"));
                }
                else {
                    view.setBackgroundColor(Color.parseColor("#F06292"));
                }

                return view;
            }
        };
        moodListView.setAdapter(adapter);

        moodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mood = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(Mood.this, mood, Toast.LENGTH_SHORT).show();
            }
        });*/

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
        getMenuInflater().inflate(R.menu.mood, menu);
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
            Toast.makeText(Mood.this, "Signed out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Mood.this, LoginScreen.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(Mood.this, Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_blogs) {
            Intent intent = new Intent(Mood.this, BlogList.class);
            startActivity(intent);
        } else if (id == R.id.nav_watch) {
            Intent intent = new Intent(Mood.this, Watch.class);
            startActivity(intent);
        } else if (id == R.id.nav_mood) {

        } else if (id == R.id.nav_counsellor) {
            Intent intent = new Intent(Mood.this, Counsellor.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Download this awesome app. http://www.google.co.in");
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent,"Share Application" ));
        } else if (id == R.id.nav_connect) {
            Intent intent = new Intent(Mood.this, Connect.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
