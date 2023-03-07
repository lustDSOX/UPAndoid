package com.example.up;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Onboarding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
    }

    public void GoLog(View v){
        Intent intent = new Intent(Onboarding.this,Login.class);
        startActivity(intent);
    }

    public void GoReg(View v){
      //Intent intent = new Intent(Onboarding.this,Reg.class);
        // startActivity(intent);
    }

}