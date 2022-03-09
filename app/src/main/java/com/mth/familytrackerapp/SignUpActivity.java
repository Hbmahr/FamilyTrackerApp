package com.mth.familytrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends Activity {

    private EditText inputfirstName,inputPhone,inputEmail,inputPassword,inputconf_pass;
    private TextView btn_login;
    private ImageView image,addimage;
    private Button btn_Signup;
    private Spinner spinner;
    private String sfname,sRelation,semail,sphone,spassword,sconfpassword,stremail;
    private Uri imageuri;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

       //finding Views

        inputfirstName = findViewById(R.id.fname);
        inputPhone = findViewById(R.id.userPhone);
        spinner = findViewById(R.id.spin_relation);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputconf_pass = findViewById(R.id.confirm_password);
        btn_Signup=findViewById(R.id.btn_signup);

        //uploadImage functioncall

         uploadImage();

        //Upload Image code

        // Spinner Code
            SpinnerCall();
        //spinner code ends

        auth = FirebaseAuth.getInstance();


        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInputData();
               boolean a=validateUtils(sfname,sRelation,semail,sphone,spassword,sconfpassword);

               if(a==true){

                   auth.createUserWithEmailAndPassword(semail, spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               FirebaseDatabase rootNode=FirebaseDatabase.getInstance();
                               DatabaseReference reference=rootNode.getReference("ParentData");
                               Rigester registerUser=new Rigester(sfname,sRelation,semail,sphone);
                               reference.child(sphone).setValue(registerUser);
                               /*firebaseDatabase = FirebaseDatabase.getInstance();
                               mDatabase = firebaseDatabase.getReference("user");
                               Rigester registerUser=new Rigester(sfname,slname,sRelation,semail);
                               mDatabase.child(sfname).setValue(registerUser);*/
                               sendRegistrationLink();
                               ok();
                           } else {
                               Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                       Toast.LENGTH_SHORT).show();
                               Log.e("Failed", task.getException() + "");
                           }
                       }
                   });
               }


            }
        });


        btn_login=findViewById(R.id.btn_login);
        btn_login.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

    }

    private void RegisterParent(String sfname, String sphone, String sRelation, String semail) {

        Rigester parent=new Rigester();
        parent.setFname(sfname);
        parent.setPhone(sphone);
        parent.setRelation(sRelation);
        parent.setEmail(semail);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                mDatabase.setValue(parent);

                // after adding this data we are showing toast message.
                Toast.makeText(SignUpActivity.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(SignUpActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendRegistrationLink() {
        user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(SignUpActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        validateUsername();
                    } else {
                        /// Log.e("TAG", "sendEmailVerification", task.getException());
                        Toast.makeText(SignUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void validateUsername() {
        Toast.makeText(this, "Check E-Mail for Validation", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    private void ok() {
        try {
            String db_ref = mDatabase.getKey();
            Rigester user = new Rigester(semail, spassword);
            if (db_ref != null) {
                mDatabase.child("login").child(db_ref).setValue(user);
            }
            RegisterParent(sfname,sRelation,semail,sphone);
            Toast.makeText(SignUpActivity.this, "SignUp Successfully", Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }



    private void getInputData() {
        sfname = inputfirstName.getText().toString();
        sphone = inputPhone.getText().toString();
        semail = inputEmail.getText().toString();
        spassword = inputPassword.getText().toString();
        sconfpassword = inputconf_pass.getText().toString();
    }

    private boolean validateUtils(String sfname, String sRelation, String semail, String sphone,String spassword ,String sconfpassword) {

        if (TextUtils.isEmpty(sfname)) {
            inputfirstName.setError("Enter FirstName");
            return false;
        }
        if (TextUtils.isEmpty(sphone)) {
            inputPhone.setError("Enter phone");
            Toast.makeText(getApplicationContext(), sphone, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(sRelation)) {
            ((TextView)spinner.getSelectedView()).setError("Select Relation");
            return false;
        }
        if (TextUtils.isEmpty(semail)) {
            inputEmail.setError("Enter Email address");
            return false;
        }
        if (!Validate_email(semail)) {
            inputEmail.setError("Enter valid Email address");
            return false;
        }
        if (TextUtils.isEmpty(spassword)) {
            inputPassword.setError("Enter password");
            return false;
        }
        if (TextUtils.isEmpty(sconfpassword)) {
            inputconf_pass.setError("Conform password");
            return false;
        }
       if(!spassword.equals(sconfpassword)){
           inputconf_pass.setError("password not matched");
            return false;
        }
       return true;
    }

    private void uploadImage() {
        image=findViewById(R.id.Image);
        addimage=findViewById(R.id.Addimage);

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    private void SpinnerCall() {

        spinner.setSelection(1, false);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.relation, R.layout.color_spinner_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdownlayout);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if(parent.getItemAtPosition(i).equals("Relation")){
                    ((TextView) view).setTextColor(Color.GRAY);
                    sRelation="";
                }
                else
                {
                    sRelation=parent.getItemAtPosition(i).toString();
                    ((TextView) view).setTextColor(Color.BLACK);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (sRelation.equals("Relation")) {
                    Toast.makeText(getApplicationContext(), "Select Relation", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void chooseImage() {

        Intent imageintent=new Intent();
        imageintent.setType("image/*");
        imageintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imageintent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageuri=data.getData();
            image.setImageURI(imageuri);
        }
    }

    public boolean Validate_email (String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }


}

