package com.example.profile.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.profile.R;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;

public class Home extends AppCompatActivity {
    private StitchAppClient client;
    private RemoteMongoClient mongoClient;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        int content=getResources().getConfiguration().orientation;
        if(content== Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        final Intent intent=new Intent(Home.this, Login.class);

        Thread th=new Thread()
        {
            @Override
            public void run() {
                try {

                    sleep(1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        th.start();





    }
}
