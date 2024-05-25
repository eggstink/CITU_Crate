package com.example.citu_crate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
    Button signIn;
    private FirebaseAuth auth;
    EditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signIn = (Button) findViewById(R.id.btnSignin);
        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(LogIn.this, "Enter Student ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(LogIn.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userPassword.length() < 8) {
                    Toast.makeText(LogIn.this, "Password should be atleast 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(LogIn.this, MainActivity.class));
                        }else{
                            Toast.makeText(LogIn.this, "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
                }
        });
    }
    public void signUp(View view){
        startActivity(new Intent(LogIn.this, RegistrationActivity.class));
    }
}