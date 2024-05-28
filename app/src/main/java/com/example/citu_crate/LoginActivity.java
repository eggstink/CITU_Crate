package com.example.citu_crate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {
    Button signIn;
    private FirebaseAuth auth;
    EditText email, password;
    TextView signUpRegister;
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
        signUpRegister = (TextView) findViewById(R.id.signUpNoAcc);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                if(TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
                    if (TextUtils.isEmpty(userEmail) && TextUtils.isEmpty(userPassword)) {
                        Toast.makeText(LoginActivity.this, "Please enter your email address and password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(userPassword)) {
                        Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(userEmail)){
                        Toast.makeText(LoginActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (userPassword.length() < 8) {
                    Toast.makeText(LoginActivity.this, "Password should be atleast 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "You inputted an incorrect email address or password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
                }
        });

        signUpRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegistrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(toRegistrationIntent);
            }
        });
    }
}