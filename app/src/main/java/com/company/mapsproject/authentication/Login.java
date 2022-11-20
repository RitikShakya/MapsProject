package com.company.mapsproject.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Login extends AppCompatActivity {

    TextView username_var,password_var,signupbutton,forgotPass;
    Button loginbutton;
    //TextInputEditText password;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

         username_var =(TextView) findViewById(R.id.usernames);
         password_var = (TextView) findViewById(R.id.passwords);
        forgotPass = findViewById(R.id.forgotpass);

       // password = findViewById(R.id.password_toggle1);
         loginbutton = findViewById(R.id.logingbtnsignup);
         signupbutton =  findViewById(R.id.signupbtn);
         firebaseAuth = FirebaseAuth.getInstance();



        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_ = username_var.getText().toString();
                String password_ = password_var.getText().toString();

                if(!username_.isEmpty()){
                    username_var.setError(null);
                    if(!password_.isEmpty()){
                        password_var.setError(null);

                        final String username_data =username_var.getText().toString();
                        final String password_data = password_var.getText().toString();

//                        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
//                        DatabaseReference databaseReference = firebaseDatabase.getReference("datauser");

                        firebaseAuth.signInWithEmailAndPassword(username_data,password_data).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(),"You are not registered Or Please check the details",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


 //                       Query check_username= databaseReference.orderByChild("username").equalTo(username_data);
//                        check_username.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if(dataSnapshot.exists()){
//                                    username_var.setError(null);
//                                    //username_var.setEnabled(false);
//                                    String passwordcheck = dataSnapshot.child(username_data).child("password").getValue(String.class);
//                                    if(passwordcheck.equals(password_data)){
//                                        password_var.setError(null);
//                                       // password_var.setEnabled(false);
//                                        Toast.makeText(getApplicationContext(),"success login",Toast.LENGTH_SHORT).show();
//
//                                        Intent intent= new Intent(getApplicationContext(),Dashboard.class);
//                                        startActivity(intent);
//                                        finish();
//
//                                    }else{
//                                        password_var.setError("wrong password");
//
//                                    }
//
//                                }else{
//                                    username_var.setError("username not exists");
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
                    }else{
                        password_var.setError("enter password");
                    }

                }else{
                    username_var.setError("please enter username");
                }
            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);


            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });


    }
}