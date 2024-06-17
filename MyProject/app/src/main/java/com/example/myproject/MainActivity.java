package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth auth;

    private TextView detailsTextView;
    FirebaseUser user;

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
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        //logoutButton = findViewById(R.id.logout_btn);
        LayoutInflater inflater = LayoutInflater.from(this);
        View otherLayout = inflater.inflate(R.layout.header_user_menu, null);
        detailsTextView = otherLayout.findViewById(R.id.user_name_text);
        user = auth.getCurrentUser();


        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else{
            detailsTextView.setText("Hello " + user.getEmail());
        }


        drawerLayout = findViewById(R.id.drawer_layout_user);
        navigationView = findViewById(R.id.navigationViewUser);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.menu_open, R.string.close_menu);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.nav_home_user:
                        Log.i("MENU_DRAWER_TAG","Home item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new HomeUserFragment());
                        break;
                    case R.id. nav_logout_user:
                        Log.i("MENU_DRAWER_TAG","Logout item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        FirebaseAuth.getInstance().signOut();
                        loadLogin();
                        break;
                }

                return true;
            }
        });
        replaceFragment(new HomeUserFragment());
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

            fragmentTransaction.replace(R.id.fragment_container_user, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    // method to load login activity
    public void loadLogin(){
        Intent intent = new Intent(getApplication(),LoginActivity.class);
        startActivity(intent);
        finish();
    }
}