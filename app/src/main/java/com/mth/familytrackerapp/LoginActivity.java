package com.mth.familytrackerapp;

import static com.mth.familytrackerapp.AddChildActivity.ChildData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class LoginActivity extends Activity {

    private EditText Email, Password;
    private FirebaseAuth db_auth;
    private ProgressBar progressBar;
    private Button btn_Login;
    public static final String DB_PATH = "login";
    private DatabaseReference db_reff;
    DatabaseReference reffer;
    FirebaseUser username;
    private TextView btn_Signup,btn_forgetpass,logintext;
    private SharedPreferences sharedPreferences;
    String childname,childRelation,childEmail,childPhone,parentPhone;
    boolean exsit=false;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_Signup=findViewById(R.id.btn_signup);
        btn_forgetpass=findViewById(R.id.btn_forgot);
        logintext=findViewById(R.id.logintext);

        //will receive Chiled data if user adding child
        receivePreferences();

        btn_Signup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });
        btn_forgetpass.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotpasswordActivity.class));
            finish();
        });
        username = FirebaseAuth.getInstance().getCurrentUser();

        reffer = FirebaseDatabase.getInstance().getReference().child("login");
        db_reff = FirebaseDatabase.getInstance().getReference(DB_PATH);

        if (username != null) {
            boolean verify_email = username.isEmailVerified();
            if (verify_email) {
                startActivity(new Intent(LoginActivity.this, TrackChildActivity.class));
                this.finish();

            }
        }
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        btn_Login = findViewById(R.id.btn_login);

        //Get Firebase auth instance

        db_auth = FirebaseAuth.getInstance();

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(childname!=null)
                {
                    Toast.makeText(LoginActivity.this, ""+parentPhone, Toast.LENGTH_SHORT).show();
                    datasaved();
                    login_child();

                }
                else
                login();
            }
        });

    }

    private void receivePreferences() {
        sharedPreferences=getSharedPreferences(ChildData,MODE_PRIVATE);
        childname=sharedPreferences.getString("childfirstName",null);
        childRelation=sharedPreferences.getString("childRelation",null);
        childEmail=sharedPreferences.getString("childEmail",null);
        childPhone=sharedPreferences.getString("childPhone",null);
        parentPhone=sharedPreferences.getString("parentPhone",null);
        if(childname!=null) {
            logintext.setVisibility(View.VISIBLE);
        }
    }

    private void login_child() {

        String mail = Email.getText().toString();
        final String pass = Password.getText().toString();

        validateUtils(mail,pass);
        progressBar.setVisibility(View.VISIBLE);

        db_auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);

                if (!task.isSuccessful()) {
                    // there was an error
                    if (pass.length() < 8) {
                        Password.setError(getString(R.string.password_length));
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.authentication_failed), Toast.LENGTH_LONG).show();
                    }
                } else {
                    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                    boolean emailVerified = user1.isEmailVerified();
                    if (emailVerified) {

                            datasaved();
                            if(exsit==true){
                                logout();
                                startActivity(new Intent(LoginActivity.this, Child_MainActivity.class));
                                finish();
                            }
                            else {
                                startActivity(new Intent(LoginActivity.this,AddChildActivity.class));
                                Toast.makeText(LoginActivity.this, "Parent phone not exsit please check again", Toast.LENGTH_LONG).show();


                            }


                    } else {
                        Toast.makeText(LoginActivity.this, "Verify E-Mail First", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void datasaved() {

        FirebaseDatabase instance= FirebaseDatabase.getInstance();
        DatabaseReference rootNode = instance.getReference("ParentData");
        RegisterChild registerChild=new RegisterChild(childname,childRelation,childEmail,childPhone);
        rootNode.child(parentPhone).child(childPhone).setValue(registerChild);



       /* rootNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot datasnapshot : snapshot.getChildren()){

                    String Pphone=datasnapshot.child("phone").getValue().toString();
                    if(Pphone.equals(parentPhone)){
                        RegisterChild registerChild=new RegisterChild(childname,childRelation,childEmail,childPhone);
        rootNode.child(parentPhone).child(childPhone).setValue(registerChild);
                        exsit=true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database Not connected", Toast.LENGTH_SHORT).show();

            }
        });
*/


        //DatabaseReference reference = rootNode.getReference("ParentData");

    }
    private void validateUtils(String mail, String pass) {
        if (TextUtils.isEmpty(mail)) {
            Email.setError("Enter email address!");
            //    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Validate_email(mail)) {
            Toast.makeText(LoginActivity.this, "Enter Valid E-Mail Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            Password.setError("Enter password!");
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void login() {
        String mail = Email.getText().toString();
        final String pass = Password.getText().toString();
        validateUtils(mail,pass);

        progressBar.setVisibility(View.VISIBLE);

        db_auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                if (!task.isSuccessful()) {
                    // there was an error
                    if (pass.length() < 8) {
                        Password.setError(getString(R.string.password_length));
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.authentication_failed), Toast.LENGTH_LONG).show();
                    }
                } else {
                    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                    boolean emailVerified = user1.isEmailVerified();
                    if (emailVerified) {
                        startActivity(new Intent(LoginActivity.this, TrackChildActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Verify E-Mail First", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public boolean Validate_email (String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
    private void logout() {
        reffer = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        auth.signOut();
    }
}