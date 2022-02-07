package com.example.personaltodolist_starter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //1.5 seconds
                    sleep(1500);
                    //then start the next activity
                    //DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("hi");
                    //dr.setValue("bye");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    //  overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}