package com.mth.familytrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class ForgotpasswordActivity extends Activity {

    private ImageView btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        btn_back=findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(ForgotpasswordActivity.this, LoginActivity.class));
            finish();
        });
    }
}