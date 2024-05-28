package com.example.citu_crate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress {
    RecyclerView recyc;
    private List<AddressModel> addressModelList;
    private AddressAdapter addAdap;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button btnaddAddress, btnPayment;
    Toolbar toolbar;
    String mAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Toolbar
        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize RecyclerView and Adapter
        recyc = findViewById(R.id.address_recycler);
        recyc.setLayoutManager(new LinearLayoutManager(this));
        addressModelList = new ArrayList<>();
        addAdap = new AddressAdapter(this, addressModelList, this);
        recyc.setAdapter(addAdap);

        // Initialize Buttons
        btnPayment = findViewById(R.id.payment_btn);
        btnaddAddress = findViewById(R.id.add_address_btn);

        // Check if the previous activity was CartActivity
        boolean fromCart = getIntent().getBooleanExtra("fromCart", false);
        if (!fromCart) {
            btnPayment.setVisibility(View.GONE);
        }

        // Fetch addresses from Firestore
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                AddressModel addressModel = doc.toObject(AddressModel.class);
                                addressModelList.add(addressModel);
                                addAdap.notifyDataSetChanged();
                            }
                        }
                    }
                });

        // Set click listener for payment button
        // Set click listener for payment button
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if user has selected an address
                if (mAddress.isEmpty()) {
                    // Display a toast message indicating that the user needs to add an address first
                    Toast.makeText(AddressActivity.this, "Please add an address first", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                    startActivity(intent);
                }
            }
        });


        // Set click listener for add address button
        btnaddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this, AddAddressActivity.class));
            }
        });
    }

    @Override
    public void setAddress(String address) {
        mAddress = address;
    }


}


