package com.foodapp.murti.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.SearchView;
import android.widget.TextView;

import com.foodapp.murti.Fragments.CartFragment;
import com.foodapp.murti.Fragments.ChangePwd;
import com.foodapp.murti.Fragments.History;
import com.foodapp.murti.Fragments.HomeFagment;
import com.foodapp.murti.Fragments.MyAccounts;
import com.foodapp.murti.Fragments.ReferAFriend;
import com.foodapp.murti.Fragments.SubCatagoryFragment;
import com.foodapp.murti.Fragments.SubCatagoryFragmentPage;
import com.foodapp.murti.Fragments.UpdateProfile;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.DatabaseHandler;
import com.foodapp.murti.Utils.Getseter;

import java.util.ArrayList;
import java.util.List;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mCounter;
    Fragment fragment;
    private int count=0;
    public static MenuItem countNumber;
    TextView name,email,edit;
    public static TextView textOne;
    DatabaseHandler db;
    List<Getseter> DataList=new ArrayList<Getseter>();
    SearchView simpleSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        name=(TextView)header.findViewById(R.id.name);
        email=(TextView)header.findViewById(R.id.email);
        edit=(TextView)header.findViewById(R.id.edit);

        textOne=(TextView)findViewById(R.id.textOne);
        simpleSearchView=(SearchView) findViewById(R.id.simpleSearchView);

        db = new DatabaseHandler(getApplicationContext());
        DataList=db.getAllCatagory();
        textOne.setText(DataList.size()+"");
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //toolbar.getNavigationIcon().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        try {
            name.setText(Getseter.preferences.getString("uname","").toUpperCase());
            email.setText(Getseter.preferences.getString("emailid",""));

            Log.d("sdfgdgdfhbdfh",Getseter.preferences.getString("city_id",""));
            Log.d("sdfgdgdfhbdfh",Getseter.preferences.getString("city_name",""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setTitle("Murti");



        fragment=new HomeFagment();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();


        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                //searchApi(s);
                Fragment fragment=new SubCatagoryFragment();
                FragmentManager manager=getSupportFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                Bundle bundle=new Bundle();
                bundle.putString("cat_id","");
                bundle.putString("type","search");
                bundle.putString("query",s);
                fragment.setArguments(bundle);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new UpdateProfile();
                FragmentManager fm=getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("type","none");
                bundle.putString("orderitem", "");
                bundle.putString("cal_price","");
                bundle.putInt("length",0);
                FragmentTransaction ft=fm.beginTransaction();
                fragment.setArguments(bundle);
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame, fragment).addToBackStack(null);
                ft.commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

    }

    private void searchApi(String str) {


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
        getMenuInflater().inflate(R.menu.navigation, menu);
        countNumber = menu.findItem(R.id.countNumber);

//        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.badge).getActionView();
//        mCounter = (TextView) badgeLayout.findViewById(R.id.counter);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item) {
//            countNumber.setTitle("2");
//            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
//            if (mCounter!=null) {
//                count++;
//                mCounter.setText("+"+Integer.toString(count));
//            }

            fragment=new CartFragment();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
             ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            fragment=new HomeFagment();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
             ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();

        }
        else if (id == R.id.account) {
            fragment=new MyAccounts();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
             ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();

        }
        else if (id == R.id.referal) {
            fragment=new ReferAFriend();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();

        }
        else if (id == R.id.history) {
            fragment=new History();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
             ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();

        }
// else if (id == R.id.breakfirst) {
//
//        } else if (id == R.id.lunch) {
//
//        } else if (id == R.id.diner) {
//
//        }
        else if (id == R.id.changepwd) {

            fragment=new ChangePwd();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
             ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.logout) {
            Getseter.editor.clear();
            Getseter.editor.commit();
            startActivity(new Intent(Navigation.this,Login.class));
            finishAffinity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
