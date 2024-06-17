package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.menu_open,R.string.close_menu);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Log.i("MENU_DRAWER_TAG","Home item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new AdminHomeFragment());
                        break;
                    case R.id.nav_search:
                        Log.i("MENU_DRAWER_TAG","Search item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new AdminSearchFragment());
                        break;
                    case R.id.nav_candidates:
                        Log.i("MENU_DRAWER_TAG","Candidates item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new AdminCandidateFragment());
                        break;
                    case R.id.nav_users:
                        Log.i("MENU_DRAWER_TAG","Users item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new AdminUserFragment());
                        break;
                    case R.id.nav_logout:
                        Log.i("MENU_DRAWER_TAG","Logout item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        FirebaseAuth.getInstance().signOut();
                        loadLogin();
                        break;
                }



                return true;
            }
        });
        replaceFragment(new AdminHomeFragment());
    }
    public void loadLogin(){
        Intent intent = new Intent(getApplication(),LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragments != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            for (Fragment frag : fragments) {
                if (frag != null && !frag.equals(fragment) && frag.isAdded()) {
                    fragmentTransaction.remove(frag);
                }
            }

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}