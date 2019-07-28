package com.susy_xu.susy_todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.susy_xu.susy_todo.mainFragments.AllFragment;
import com.susy_xu.susy_todo.mainFragments.DailyFragment;
import com.susy_xu.susy_todo.mainFragments.DateFragment;
import com.susy_xu.susy_todo.mainFragments.DoneFragment;
import com.susy_xu.susy_todo.mainFragments.EntertainFragment;
import com.susy_xu.susy_todo.mainFragments.MeetingFragment;
import com.susy_xu.susy_todo.mainFragments.StudyFragment;
import com.susy_xu.susy_todo.settings.SettingActivity;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBar mActionBar;
    Fragment mFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle("全部");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = new AllFragment();
        ft.replace(R.id.mainFragment_layout, mFragment);
        ft.commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("updateORInsert", "insert");
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true); //默认在类型all
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_all) {
            mActionBar.setTitle("全部");
            mFragment = new AllFragment();
        } else if (id == R.id.nav_meeting) {
            mActionBar.setTitle("会议");
            mFragment = new MeetingFragment();
        } else if (id == R.id.nav_study) {
            mActionBar.setTitle("学习");
            mFragment = new StudyFragment();
        } else if (id == R.id.nav_date) {
            mActionBar.setTitle("约会");
            mFragment = new DateFragment();
        } else if (id == R.id.nav_daily) {
            mActionBar.setTitle("生活");
            mFragment = new DailyFragment();
        } else if (id == R.id.nav_entertain) {
            mActionBar.setTitle("娱乐");
            mFragment = new EntertainFragment();
        } else if (id == R.id.nav_done) {
            mActionBar.setTitle("已完成");
            mFragment = new DoneFragment();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }

        ft.replace(R.id.mainFragment_layout, mFragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}