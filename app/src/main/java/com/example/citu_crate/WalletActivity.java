package com.example.citu_crate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class WalletActivity extends AppCompatActivity {
    TextView cashAmount;
    Button toTopUp;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wallet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cashAmount = findViewById(R.id.text_walletamount);
        firestore = FirebaseFirestore.getInstance();
        toTopUp = findViewById(R.id.btnToTopUp);
        auth = FirebaseAuth.getInstance();

        toTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topUp = new Intent(WalletActivity.this, TopUpActivity.class);
                startActivity(topUp);
            }
        });

        FirebaseUser currentUser = auth.getCurrentUser();

        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("Wallet")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Initialize cashAmount variable
                            double cash = 0.0;

                            // Iterate through the documents in the result
                            for (DocumentSnapshot document : task.getResult()) {
                                // Get the cash amount from the document
                                Double dbcash = document.getDouble("cash");
                                if (dbcash != null) {
                                    // Update cashAmount if the value is not null
                                    cash = dbcash;
                                    break;
                                }
                            }

                            // Set the cash amount in your TextView
                            cashAmount.setText(String.valueOf(cash));
                        } else {
                            // Handle Firestore query failure
                            Log.e("WalletActivity", "Error getting documents", task.getException());
                        }
                    }
                });

    }

}
