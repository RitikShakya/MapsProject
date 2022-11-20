package com.company.mapsproject.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.company.mapsproject.MainActivity;
import com.company.mapsproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    EditText email,confirmPass,password;
    Button registerbutton;
    TextView loginbutton, forgotPass ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);


        email=  findViewById(R.id.email);

        confirmPass = findViewById(R.id.confirmPass);
        password=findViewById(R.id.passwords);
        forgotPass = findViewById(R.id.forgotpass);

        registerbutton= findViewById(R.id.registterbtn);
        loginbutton= findViewById(R.id.logingbtnsignup);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference("datauser");

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerbutton(view);



            }

        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginpage(view);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }


    public void loginpage(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    public void registerbutton(View view) {

        String email_ = email.getText().toString();

        String password_ = password.getText().toString();
        String confirmPass_ = confirmPass.getText().toString();


        if (!email_.isEmpty()) {
            email.setError(null);

            if (!password_.isEmpty()) {
                password.setError(null);

                if(!confirmPass_.isEmpty() ) {
                    confirmPass.setError(null);

                    if(confirmPass_.equals(password_)) {
                        confirmPass.setError(null);


                        String email_s = email.getText().toString();

                        String password_s = password.getText().toString();


                        signupwithemail(email_s, password_s);

                    }else{
                        confirmPass.setError("enter confirm pass correctly");
                    }
                }else{
                    confirmPass.setError(" enter confirm pass ");
                }

            } else {
                password.setError("enter password");
            }

        } else {
            email.setError("enter email");
        }

    }



    public void signupwithemail(String email_s, String password_s)
    {

        firebaseAuth.createUserWithEmailAndPassword(email_s,password_s).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                   // uuid = UUID.randomUUID();
                    UserData userdata = new UserData(email_s, password_s);
                    databaseReference.child("user").child(String.valueOf(firebaseAuth.getUid())).setValue(userdata);
                    Toast.makeText(getApplicationContext(),"registered success",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();


                }else {


                    Toast.makeText(getApplicationContext(),"registered not success",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
