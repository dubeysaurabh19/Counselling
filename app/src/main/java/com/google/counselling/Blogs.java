package com.google.counselling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blogs extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    BlogModel blog;

    ListView blogList;

    List<String> blogData = new ArrayList<>();

    Map<String,String> map = new HashMap<>();

    String category;


    private static final String TAG = "Blog";
    //List<BlogModel> blogs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);
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
        category = intent.getStringExtra("Category");

        blogList = (ListView) findViewById(R.id.blogTopicListView);

        //addBlog();

        displayBlogTitle();

        blogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String blog = String.valueOf(parent.getItemAtPosition(position));
                //Toast.makeText(Blogs.this, blog, Toast.LENGTH_SHORT).show();
                String link = map.get(blog);
                Intent intent = new Intent(Blogs.this, BlogDisplay.class);
                intent.putExtra("Link", link);
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
        getMenuInflater().inflate(R.menu.blogs, menu);
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
            Toast.makeText(Blogs.this, "Signed out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Blogs.this, LoginScreen.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(Blogs.this, Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_blogs) {
            Intent intent = new Intent(Blogs.this, BlogList.class);
            startActivity(intent);
        } else if (id == R.id.nav_watch) {
            Intent intent = new Intent(Blogs.this, Watch.class);
            startActivity(intent);
        } else if (id == R.id.nav_mood) {
            Intent intent = new Intent(Blogs.this, Mood.class);
            startActivity(intent);
        } else if (id == R.id.nav_counsellor) {
            Intent intent = new Intent(Blogs.this, Counsellor.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Download this awesome app. http://www.google.co.in");
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent,"Share Application" ));

        } else if (id == R.id.nav_connect) {
            Intent intent = new Intent(Blogs.this, Connect.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void displayBlogTitle (){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference databaseReference = database.getReference().child("blogs");
        //String blogID = databaseReference.getKey();

        //DatabaseReference reference = database.getReference("/blogs/");

        DatabaseReference reference = database.getReference("/blogs/" + category);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                blog = dataSnapshot.getValue(BlogModel.class);
                blogData.add(blog.getTitle());
                map.put(blog.getTitle(),blog.getLink());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Blogs.this, android.R.layout.simple_list_item_1, blogData){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view =  super.getView(position, convertView, parent);
                        /*if(position%2 == 1) {
                            view.setBackgroundColor(Color.parseColor("#C5E1A5"));
                        }
                        else {
                            view.setBackgroundColor(Color.parseColor("#AED581"));
                        }*/
                        TextView text = (TextView) view.findViewById(android.R.id.text1);
                        text.setTextColor(Color.WHITE);
                        return view;
                    }

                };
                blogList.setAdapter(adapter);
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

        /*reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                blog = dataSnapshot.getValue(BlogModel.class);
                Log.d(TAG, blog.getTitle());
                //String blogs[] = new String[] {blog.getTitle()};
                List<String> blogs = new ArrayList<String>();
                blogs.add(blog.getTitle());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Blogs.this, android.R.layout.simple_list_item_1, blogs);
                blogList.setAdapter(adapter);
                //blogs.add(blog);
                Toast.makeText(Blogs.this, blog.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    void addBlog() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        DatabaseReference blogReference = reference.child("blogs");
        DatabaseReference newBlog = blogReference.push();
        //newBlog.setValue(new BlogModel("Ways to cure stress", "http://worldwidelaugh.com/2017/04/25/ways-cure-stress/"));
        //newBlog.setValue(new BlogModel("Things only 90s kids can relate to", "http://worldwidelaugh.com/2017/06/17/things-90s-kids-can-relate/"));
        //newBlog.setValue(new BlogModel("Is my choice correct?", "http://worldwidelaugh.com/2017/03/27/choice-for-your-future/"));
        //newBlog.setValue(new BlogModel("Ways to cure depression", "http://worldwidelaugh.com/2017/03/19/7-ways-cure-depression/"));

    }
}