package com.csecu.amrit.ctgrestaurants.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.SplashActivity;
import com.csecu.amrit.ctgrestaurants.controllers.NetworkController;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.fragments.LoginFragment;
import com.csecu.amrit.ctgrestaurants.fragments.RegistrationFragment;
import com.csecu.amrit.ctgrestaurants.fragments.ViewFoodsFragment;
import com.csecu.amrit.ctgrestaurants.fragments.ViewRestaurantsFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NetworkController networkController;
    ToastController toastController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        // Boolean status = sharedpreferences.getBoolean(getString(R.string.auth), false);
        editor.putBoolean(getString(R.string.auth), false);
        editor.commit();

        networkController = new NetworkController(this);
        toastController = new ToastController(this);
        if (networkController.isNetworkAvailable()) {
            ViewRestaurantsFragment fragment = new ViewRestaurantsFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.home_container, fragment);
            transaction.commit();
        } else {
            toastController.errorToast("Check your internet connection");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_dev) {
            Intent intent = new Intent(HomeActivity.this, DevActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home_restaurants) {
            networkController = new NetworkController(this);
            toastController = new ToastController(this);
            if (networkController.isNetworkAvailable()) {
                ViewRestaurantsFragment fragment = new ViewRestaurantsFragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_container, fragment);
                transaction.commit();
            } else {
                toastController.errorToast("Check your internet connection");
            }
        } else if (id == R.id.home_foods) {
            networkController = new NetworkController(this);
            toastController = new ToastController(this);
            if (networkController.isNetworkAvailable()) {
                ViewFoodsFragment fragment = new ViewFoodsFragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_container, fragment);
                transaction.commit();
            } else {
                toastController.errorToast("Check your internet connection");
            }
        } else if (id == R.id.home_nearby) {
            networkController = new NetworkController(this);
            toastController = new ToastController(this);
            if (networkController.isNetworkAvailable()) {

            } else {
                toastController.errorToast("Check your internet connection");
            }
        } else if (id == R.id.home_login) {
            LoginFragment fragment = new LoginFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.home_container, fragment);
            transaction.commit();
        } else if (id == R.id.home_reg) {
            RegistrationFragment fragment = new RegistrationFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.home_container, fragment);
            transaction.commit();
        }
        /*else if (id == R.id.own_add_food) {
            AddFoodFragment fragment = new AddFoodFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.home_container, fragment);
            transaction.commit();
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
