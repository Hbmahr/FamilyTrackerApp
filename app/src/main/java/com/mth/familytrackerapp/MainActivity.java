package com.mth.familytrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button btn_parent,btn_child;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_parent=findViewById(R.id.btnparent);
        btn_child=findViewById(R.id.btnchild);

        btn_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Log.e("Failed", " not shown");
            }
        });

        btn_child.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddChildActivity.class));
        });

    }
}