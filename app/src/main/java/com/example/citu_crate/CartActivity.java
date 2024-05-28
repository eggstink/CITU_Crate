package com.example.citu_crate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    int overAllTotalAmount;
    Button btnCheckout;
    TextView overAllAmount;
    Toolbar toolbar;
    RecyclerView recyc;
    List<MyCartModel> cartModelList;
    MyCartAdapter cartAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.my_cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

        overAllAmount = findViewById(R.id.textView3);
        recyc = findViewById(R.id.cart_rec);
        recyc.setLayoutManager(new LinearLayoutManager(this));
        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(this, cartModelList);
        recyc.setAdapter(cartAdapter);
        btnCheckout = findViewById(R.id.buy_now);

        loadCartItems();

        btnCheckout.setOnClickListener(v -> {
            if (cartModelList.isEmpty()) {
                Toast.makeText(CartActivity.this, "Your cart is empty, please shop first", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CartActivity.this, MainActivity.class));
            } else {
                Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                intent.putExtra("fromCart", true);
                startActivity(intent);
            }
        });
    }

    private void loadCartItems() {
        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            cartModelList.clear();
                            int totalAmount = 0;
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                if (myCartModel != null) {
                                    myCartModel.setDocumentId(doc.getId());
                                    cartModelList.add(myCartModel);

                                    int price = Integer.parseInt(myCartModel.getProductPrice());
                                    int quantity = Integer.parseInt(myCartModel.getTotalQuantity());
                                    totalAmount += price * quantity;
                                }
                            }
                            overAllTotalAmount = totalAmount;
                            overAllAmount.setText("Total Amount: ₱" + overAllTotalAmount);
                            cartAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalBill = intent.getIntExtra("totalAmount", 0);
            overAllAmount.setText("Total Amount: ₱" + totalBill);
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
