package com.example.citu_crate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalletActivity extends AppCompatActivity {
    TextView cashAmount;
    Button toTopUp;
    FirebaseFirestore firestore;

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

        Intent topUp = new Intent(WalletActivity.this, TopUpActivity.class);
        startActivity(topUp);
        finish();

        firestore.collection("users").document("yourUserId")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Get cash amount from Firestore document
                            Double cashMoney = document.getDouble("cash");
                            // Set cash amount to TextView
                            if (cashAmount != null) {
                                cashAmount.setText(String.valueOf(cashMoney));
                            }
                        } else {
                            // Document does not exist
                            cashAmount.setText("N/A");
                        }
                    } else {
                        // Error retrieving document
                        cashAmount.setText("Error");
                    }
                });

        toTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topUp = new Intent(WalletActivity.this, TopUpActivity.class);
                startActivity(topUp);
                finish();
            }
        });

    }
}
