package com.csecu.amrit.ctgrestaurants.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.csecu.amrit.ctgrestaurants.controllers.NetworkController;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.fragments.AddFoodFragment;
import com.csecu.amrit.ctgrestaurants.fragments.BookingFragment;
import com.csecu.amrit.ctgrestaurants.fragments.NotifyFragment;
import com.csecu.amrit.ctgrestaurants.fragments.ResDetailsFragment;
import com.csecu.amrit.ctgrestaurants.fragments.ViewBookingFragment;
import com.csecu.amrit.ctgrestaurants.fragments.ViewFoodsFragment;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;

public class OwnerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ResAndOwner resAndOwner;
    NetworkController networkController;
    ToastController toastController;
    Bundle bundle;
    private static int TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundle = getIntent().getExtras();
        networkController = new NetworkController(this);
        toastController = new ToastController(this);
        if (networkController.isNetworkAvailable()) {
            if (bundle != null) {
                resAndOwner = bundle.getParcelable("ResAndOwner");
                ViewFoodsFragment fragment = new ViewFoodsFragment();
                bundle.putParcelable("ResAndOwner", resAndOwner);
                fragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.owner_container, fragment);
                transaction.commit();
            }
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
        getMenuInflater().inflate(R.menu.owner, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        toastController = new ToastController(this);

        if (id == R.id.owner_foods) {
            networkController = new NetworkController(this);
            toastController = new ToastController(this);
            if (networkController.isNetworkAvailable()) {
                if (bundle != null) {
                    resAndOwner = bundle.getParcelable("ResAndOwner");
                    ViewFoodsFragment fragment = new ViewFoodsFragment();
                    bundle.putParcelable("ResAndOwner", resAndOwner);
                    fragment.setArguments(bundle);
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.owner_container, fragment);
                    transaction.commit();
                }
            } else {
                toastController.errorToast("Check internet connection");
            }
        } else if (id == R.id.owner_info) {
            if (bundle != null) {
                ResDetailsFragment fragment = new ResDetailsFragment();
                bundle.putParcelable("ResAndOwner", resAndOwner);
                fragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.owner_container, fragment);
                transaction.commit();
            }
        } else if (id == R.id.owner_add_food) {
            AddFoodFragment fragment = new AddFoodFragment();
            bundle.putParcelable("ResAndOwner", resAndOwner);
            fragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.owner_container, fragment);
            transaction.commit();
        } else if (id == R.id.owner_booking) {
            ViewBookingFragment fragment = new ViewBookingFragment();
            bundle.putParcelable("ResAndOwner", resAndOwner);
            fragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.owner_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.owner_offer) {
            NotifyFragment fragment = new NotifyFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.owner_container, fragment);
            transaction.commit();
        } else if (id == R.id.owner_add_user) {

        } else if (id == R.id.owner_logout) {
            SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            // Boolean status = sharedpreferences.getBoolean(getString(R.string.auth), false);
            editor.putBoolean(getString(R.string.auth), false);
            editor.commit();
            toastController = new ToastController(this);
            toastController.successToast("Sign out successful");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(OwnerActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, TIME_OUT);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
