package com.example.citu_crate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class TopUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    String[] payment = {"GCash", "Maya", "PayPal", "7-Eleven"};
    AutoCompleteTextView paymentDropDown;
    ArrayAdapter<String> adapterPayments;
    Button btnProceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_top_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        paymentDropDown = findViewById(R.id.paymentdropdown);
//        adapterPayments = new ArrayAdapter<String>(this, R.layout.activity_top_up);
        btnProceed = findViewById(R.id.btnProceed);
//        paymentDropDown.setAdapter(adapterPayments);
//        paymentDropDown.setOnClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String item = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(TopUpActivity.this, "Item:")
//            }
//        });

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("CurrentUser")
                        .document(auth.getCurrentUser().getUid())
                        .collection("Wallet").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        // Get current cash amount from the document
                                        Double currentCash = document.getDouble("cash");
                                        if (currentCash != null) {
                                            // Add 500 to the current cash amount
                                            double newCashAmount = currentCash + 500.0;

                                            // Update the cash amount in the database
                                            document.getReference().update("cash", newCashAmount)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Proceed to the next activity
                                                            Intent proceed = new Intent(TopUpActivity.this, MainActivity.class);
                                                            startActivity(proceed);
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Handle failure to update cash amount
                                                            Log.e("TopUpActivity", "Error updating cash amount", e);
                                                            // Optionally, show a toast or error message to the user
                                                        }
                                                    });
                                            break; // Exit loop after updating the first document
                                        }
                                    }
                                } else {
                                    Log.d("TopUpActivity", "Error getting documents: ", task.getException());
                                    // Handle the case where the query fails
                                    // Optionally, show a toast or error message to the user
                                }
                            }
                        });


            }
        });
    }
}