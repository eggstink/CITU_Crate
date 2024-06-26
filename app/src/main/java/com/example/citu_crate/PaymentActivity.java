package com.example.citu_crate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PaymentActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView subTotal, discount, deliveryFee, total;
    Button btnPayment;
    double amount = 0.0;
    private static final String TAG = "MyTag";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        deliveryFee = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);
        btnPayment = findViewById(R.id.pay_btn);

        // Retrieve the amount from the intent
        db.collection("AddToCart").document(mAuth.getCurrentUser().getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            double price = document.getDouble("totalPrice");
                            amount += price;
                        }
                        // Set the subtotal and total amount
                        subTotal.setText("₱" + amount);
                        amount+=10;
                        total.setText("₱" + amount);

                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user's wallet document
                db.collection("CurrentUser")
                        .document(mAuth.getCurrentUser().getUid())
                        .collection("Wallet")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // Check if the wallet document exists
                                    if (!task.getResult().isEmpty()) {
                                        DocumentSnapshot walletDocument = task.getResult().getDocuments().get(0); // Assuming there's only one wallet document per user
                                        double cash = walletDocument.getDouble("cash");

                                        // Check if the user has enough money
                                        if (cash >= amount) {
                                            // Subtract the amount from the user's cash
                                            double newCash = cash - amount;

                                            // Update the cash field in the user's wallet document
                                            walletDocument.getReference().update("cash", newCash)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> updateTask) {
                                                            if (updateTask.isSuccessful()) {
                                                                Toast.makeText(PaymentActivity.this, "Payment successful", Toast.LENGTH_SHORT).show();
                                                                removeAllItemsFromCart();
                                                                startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                                                                finish();
                                                            } else {
                                                                Toast.makeText(PaymentActivity.this, "Error occurred, please try again", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            // Insufficient funds, notify the user
                                            Toast.makeText(PaymentActivity.this, "Insufficient funds. Please Top up", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Wallet document doesn't exist
                                        Toast.makeText(PaymentActivity.this, "Wallet not found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Handle Firestore query failure
                                    Log.e(TAG, "Error getting wallet document", task.getException());
                                    Toast.makeText(PaymentActivity.this, "Error getting wallet document", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void removeAllItemsFromCart() {
        db.collection("AddToCart").document(mAuth.getCurrentUser().getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documents) {
                            document.getReference().delete();
                        }
                    }
                });
    }

}
//
//        btnPayment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new PayPalNativeCheckoutListener() {
//                    @Override
//                    public void onPayPalCheckoutStart() {
//                        Log.d(TAG, "create: ");
//                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
//                        purchaseUnits.add(
//                                new PurchaseUnit.Builder()
//                                        .amount(
//                                                new Amount.Builder()
//                                                        .currencyCode(CurrencyCode.USD)
//                                                        .value("10.00")
//                                                        .build()
//                                        )
//                                        .build()
//                        );
//                        OrderRequest order = new OrderRequest(
//                                OrderIntent.CAPTURE,
//                                new AppContext.Builder()
//                                        .userAction(UserAction.PAY_NOW)
//                                        .build(),
//                                purchaseUnits
//                        );
//                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
//                    }
//
//                    @Override
//                    public void onPayPalCheckoutSuccess(@NonNull PayPalNativeCheckoutResult payPalNativeCheckoutResult) {
//                        approval.getOrderActions().capture(new OnCaptureComplete() {
//                            @Override
//                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
//                                Log.d(TAG, String.format("CaptureOrderResult: %s", result));
//                                Toast.makeText(PaymentActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onPayPalCheckoutFailure(@NonNull PayPalSDKError payPalSDKError) {
//                        finish();
//                    }
//
//                    @Override
//                    public void onPayPalCheckoutCanceled() {
//
//                    }
//                }
//                Toast.makeText(PaymentActivity.this,"Please pay in cash on delivery",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(PaymentActivity.this,MainActivity.class));
//            }
//        });



//        btnPayment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get the user's cash from the database
//                db.collection("Users").document(mAuth.getCurrentUser().getUid())
//                        .get()
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//                                    double cash = document.getDouble("cash");
//                                    // Check if user has enough money
//                                    if (cash >= amount) {
// Subtract the amount from the user's cash
//                                        double newCash = cash - amount;
//                                        db.collection("Users").document(mAuth.getCurrentUser().getUid())
//                                                .update("cash", newCash)
//                                                .addOnCompleteListener(task1 -> {
//                                                    if (task1.isSuccessful()) {
//                                                        Toast.makeText(PaymentActivity.this, "Payment successful", Toast.LENGTH_SHORT).show();
// Remove all items from the cart
//                                                        removeAllItemsFromCart();
// Navigate to the next activity
//                                                        startActivity(new Intent(PaymentActivity.this, HomeFragment.class));
//                                                        finish();
//                                                    } else {
//                                                        Toast.makeText(PaymentActivity.this, "Error occurred, please try again", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//                                    } else {
//                                        Toast.makeText(PaymentActivity.this, "Insufficient funds", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        });
//            }
//        });
