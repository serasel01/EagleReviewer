package com.example.serasel.eagle.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serasel.eagle.Fragments.ResultFragment;
import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.example.serasel.eagle.Dialogs.ExamDialog;
import com.example.serasel.eagle.Fragments.HomeFragment;
import com.example.serasel.eagle.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationActivity extends AppCompatActivity{

    private DrawerLayout nav_drawer_layout;
    private NavigationView nav_sidebar;
    private BottomNavigationView bottomNav;
    private ActionBarDrawerToggle toggle;
    private View navHeaderView;
    private TextView tv_nav_name, tv_nav_course;
    private CircleImageView civ_nav_photo;
    private ExamDialog examDialog;
    private String topic, course;
    private Menu sidenav_subjects;
    private DatabaseReference db_ref;

    private FrameLayout frame_home;
    private FragmentTransaction fragmentTransaction;
    private HomeFragment homeFragment;
    private ResultFragment resultFragment;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        String id = SharedPrefManager.getInstance(getApplicationContext()).getKeyUserId();
        reference = FirebaseDatabase.getInstance().getReference("Students").child(id);
        reference.keepSynced(true);

        initViews();
        initFragments();
    }

    private void initFragments() {
        frame_home = findViewById(R.id.frame_home); //initialize frame layout
        homeFragment = new HomeFragment(); //create class for the fragment
        setFragment(homeFragment); //pass class of the frag here
    }

    private void setFragment(Fragment fragment) { //fragment transaction manages switching of fragments
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_home, fragment).commit(); //replace function does the switching
        //requires the frame layout and fragment being passed
    }

    private void initViews() {
        nav_drawer_layout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, nav_drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        nav_drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        //navigation view init
        nav_sidebar = (NavigationView) findViewById(R.id.nav_sidebar);
        nav_sidebar.setNavigationItemSelectedListener(new SideNavi()); //listener for choices in menu

        navHeaderView = nav_sidebar.getHeaderView(0);
        tv_nav_name = navHeaderView.findViewById(R.id.tv_nav_name);
        tv_nav_course = navHeaderView.findViewById(R.id.tv_nav_course);
        civ_nav_photo = navHeaderView.findViewById(R.id.civ_nav_photo);

        //set student's name and course at navigation view
        tv_nav_name.setText("Name: " +
                SharedPrefManager.getInstance(getApplicationContext()).getKeyUserName());
        tv_nav_course.setText("Course: " +
                SharedPrefManager.getInstance(getApplicationContext()).getKeyUserCourse());

        //load reviewee's photo
        String stu_image = SharedPrefManager.getInstance(getApplicationContext()).getKeyUserImagepath();
        Picasso.get().load(stu_image).placeholder(R.drawable.ic_login_user).into(civ_nav_photo);

        bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavi());
    }

    @Override
    public void onBackPressed() {
        if (nav_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            nav_drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    private class BottomNavi implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.botnav_home :
                    homeFragment = new HomeFragment();
                    setFragment(homeFragment);
                    return true;
                case R.id.botnav_result :
                    resultFragment = new ResultFragment();
                    setFragment(resultFragment);
                    return true;
                default:
                    return false;
            }
        }
    }

    private class SideNavi implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.item_nav_home :
                    setFragment(homeFragment);
                    nav_drawer_layout.closeDrawer(GravityCompat.START);
                    return true;
                case R.id.item_nav_references :
                    setFragment(homeFragment);
                    nav_drawer_layout.closeDrawer(GravityCompat.START);
                    return true;
                case R.id.item_nav_result :
                    resultFragment = new ResultFragment();
                    setFragment(resultFragment);
                    nav_drawer_layout.closeDrawer(GravityCompat.START);
                    return true;
                case R.id.item_nav_logout :
                    nav_drawer_layout.closeDrawer(GravityCompat.START);

                    SharedPrefManager.getInstance(getApplicationContext()).logout();

                    Intent logIntent = new Intent(NavigationActivity.this, LoginActivity.class);
                    logIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logIntent);
                    finish();
                    return true;
                default:
                    return false;
            }
        }
    }

    private class SubjectMenu implements MenuItem.OnMenuItemClickListener {
        private String gen_subject;

        public SubjectMenu(String gen_subject) {
            this.gen_subject = gen_subject;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            examDialog = new ExamDialog(NavigationActivity.this, gen_subject);
            examDialog.setCancelable(false);
            examDialog.show();
            return true;
        }
    }
}
