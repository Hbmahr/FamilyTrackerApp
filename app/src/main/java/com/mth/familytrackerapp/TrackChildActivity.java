package com.mth.familytrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrackChildActivity extends Activity {


    private TextView name;
    private RadioButton radchat,radlog,radloc;
    private Button btn_track;
    DatabaseReference reff;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_child);

        reff = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();



        name=findViewById(R.id.Naming);
        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
        spinner.setSelection(1, false);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.Name, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if(parent.getItemAtPosition(i).equals("Relation")){
                    ((TextView) view).setTextColor(Color.GRAY);
                }
                else
                {
                    String relation=parent.getItemAtPosition(i).toString();
                    ((TextView) view).setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        radchat=findViewById(R.id.radchat);
        radlog=findViewById(R.id.radlog);
        radloc=findViewById(R.id.radloc);

        btn_track=findViewById(R.id.btn_track);
        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void logout() {
        auth.signOut();
        startActivity(new Intent(TrackChildActivity.this, LoginActivity.class));
        this.finish();
    }
}