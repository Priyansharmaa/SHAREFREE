package com.example.sharefree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_ShareFree);
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                File file = new File(getApplicationContext().getFilesDir(), "userdetails1.json");
                if(file.exists()){
                    Intent i = new Intent(MainActivity.this, mainPage.class);
                    startActivity(i);
                    //finish();
                }
                else {
                    Intent i = new Intent(MainActivity.this, UserProfile.class);
                    startActivity(i);
                    //finish();
                }
            }
        },3500);
    }
}