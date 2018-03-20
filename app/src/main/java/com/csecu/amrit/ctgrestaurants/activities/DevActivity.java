package com.csecu.amrit.ctgrestaurants.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.csecu.amrit.ctgrestaurants.R;

public class DevActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Animation in = AnimationUtils.loadAnimation(DevActivity.this, R.anim.slide_down);
                CardView cardAmrit = findViewById(R.id.card_amrit);
                cardAmrit.startAnimation(in);

                CardView cardSourav = findViewById(R.id.card_sourav);
                cardSourav.startAnimation(in);

                CardView cardNahian = findViewById(R.id.card_nahian);
                cardNahian.startAnimation(in);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
