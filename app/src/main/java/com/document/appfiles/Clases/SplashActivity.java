package com.document.appfiles.Clases;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.document.appfiles.R;
import com.document.appfiles.SliderActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    protected int _splashTime = 3000;

    private Thread splashTread;
    protected void onCreate(@Nullable Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);

        setContentView(R.layout.splash_intro);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), SliderActivity.class));
            }
        }, 4000);



    }

    @Override
    protected void onStart() {
        super.onStart();
      //Intent intent = new Intent(this, SliderActivity.class);
//
      //SystemClock.sleep(3500);
      //startActivity(intent);
      //finish();
    }
}
