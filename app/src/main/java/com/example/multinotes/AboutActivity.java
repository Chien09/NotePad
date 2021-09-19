package com.example.multinotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //To hide the Menu bar (NOT Taught in class) --> SO REFERENCE: https://stackoverflow.com/questions/26492522/how-do-i-remove-the-title-bar-in-android-studio
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}