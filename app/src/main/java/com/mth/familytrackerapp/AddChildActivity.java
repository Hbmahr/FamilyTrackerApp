package com.mth.familytrackerapp;

import androidx.annotation.NonNull;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddChildActivity extends Activity {
    private ImageView btn_back;
    private Button btn_add;
    private TextInputEditText fname,email,phoneNo,parentPhone;
    private Spinner spinner;
    boolean flag=false;
    private String Relation, Pphone;
    private  String cName,cemail,cphone,cPphone;
    public static final String ChildData="ChildData";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        btn_back=findViewById(R.id.btn_back);
        spinner = findViewById(R.id.spin_relation);
        btn_add=findViewById(R.id.btn_add);
        fname=findViewById(R.id.fname);
        email=findViewById(R.id.email);
        phoneNo=findViewById(R.id.phoneNo);
        parentPhone=findViewById(R.id.parentPhoneNo);

        spinnerFuncCall();

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(AddChildActivity.this, MainActivity.class));
            clearpreferences();
            finish();
        });



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getFieldData();
                cName =fname.getText().toString();
                cemail=email.getText().toString();
                cphone=phoneNo.getText().toString();
                cPphone=parentPhone.getText().toString();

                boolean a=validateUtils(cName,cemail,Relation,cphone,cPphone);
                if(a==true)
                {
                    checkparent();
                }
            }
        });

    }

    /*private void getFieldData() {
        //checkparent();
        cName =fname.getText().toString();
        cemail=email.getText().toString();
        cphone=phoneNo.getText().toString();
        cPphone=parentPhone.getText().toString();

    }*/

    private void checkparent() {

        FirebaseDatabase instance= FirebaseDatabase.getInstance();
        DatabaseReference rootNode = instance.getReference("ParentData");
        rootNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot datasnapshot : snapshot.getChildren()){

                    Pphone=datasnapshot.child("phone").getValue().toString();
                    Toast.makeText(AddChildActivity.this, ""+Pphone, Toast.LENGTH_SHORT).show();

                    if(Pphone.equals(cPphone)){
                        SaveChildData();
                        startActivity(new Intent(AddChildActivity.this, LoginActivity.class));
                        flag=true;
                        break;
                    }

                }
                if(flag==false)
                {

                    parentPhone.setError("parent phone not Exsit");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddChildActivity.this, "Database Not connected", Toast.LENGTH_SHORT).show();

            }
        });
        //DatabaseReference reference = rootNode.getReference("ParentData");

    }

    private void clearpreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("ChildData", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear().commit();
    }


    private void spinnerFuncCall() {
        spinner.setSelection(1, false);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.child_relation, R.layout.color_spinner_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdownlayout);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if(parent.getItemAtPosition(i).equals("Relation")){
                    ((TextView) view).setTextColor(Color.GRAY);
                    Relation="";
                }
                else
                {
                    Relation=parent.getItemAtPosition(i).toString();
                    ((TextView) view).setTextColor(Color.BLACK);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (Relation.equals("Relation")) {
                    Toast.makeText(getApplicationContext(), "Select Relation", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private boolean validateUtils(String childname, String childEmail,String childrelation,String childPhone,String parentcell) {


        if (TextUtils.isEmpty(childname)) {
            fname.setError("Enter firstName");
            return false;
        }

        if (TextUtils.isEmpty(childrelation)) {
            ((TextView)spinner.getSelectedView()).setError("Select Relation");
            return false;
        }

        if (!Validate_email(childEmail)) {
            email.setError("Enter valid Email address");
            return false;
        }
        if (TextUtils.isEmpty(childPhone)) {
            phoneNo.setError("Enter PhoneNo");
            return false;
        }
        if (TextUtils.isEmpty(parentcell)) {
            parentPhone.setError("Enter parent PhoneNo");
            return false;
        }
        /*if(parentcell.equals(Pphone)){
            parentPhone.setError("Parent phone is not valid");
            return false;
        }*/
        return true;
    }

    private void SaveChildData() {

        sharedPreferences=getSharedPreferences(ChildData,MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putString("childfirstName",cName);
        editor.putString("childRelation",Relation);
        editor.putString("childEmail",cemail);
        editor.putString("childPhone",cphone);
        editor.putString("parentPhone",cPphone);
        editor.apply();
    }

    public boolean Validate_email (String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}