package com.example.citu_crate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress{
    Button addAddress;
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

        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        recyc = findViewById(R.id.address_recycler);
        btnPayment = findViewById(R.id.payment_btn);
        btnaddAddress = findViewById(R.id.add_address_btn);
        recyc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addressModelList = new ArrayList<>();
        addAdap = new AddressAdapter(getApplicationContext(),addressModelList,this);
        recyc.setAdapter(addAdap);
//        Object obj = getIntent().getSerializableExtra("item);
//        paymentbtn
//                double amnount  = 0.0;
//                if(obj instanceof NewProductsModel){
//                    NewProductsModel newProductsModel = (newProductsModel)  obj;
//                    amount = newProductsModel.getPrice();
//                }
//        if(obj instanceof PopularProductsModel){
//            PopularProductsModel popularProductsModel = (PopularProductsModel)  obj;
//            amount = popularProductsModel.getPrice();
//        }
//        if(obj instanceof ShowAllModel){
//            ShowAllModel showAllModel = (ShowAllModel)  obj;
//            amount = showAllModel.getPrice();
//        }
//        Inntent intent = new Intenr(AddressAcctivity.this, PaymentActivity.class);
//        intent.putExtra("amount",amount);
//        startActivity(
//                intent
//        );
//        onComplete
//                after Toast is succesfdful
//                startActivity(new Intent(AddAddressActivity.this, DeatailedActivity.class));
//        finish();
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot doc : task.getResult().getDocuments()){
                                AddressModel addressModel = doc.toObject(AddressModel.class);
                                addressModelList.add(addressModel);
                                addAdap.notifyDataSetChanged();
                            }
                        }
                    }
                });
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this,PaymentActivity.class));

            }
        });
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this,AddAddressActivity.class));
            }
        });
    }

    @Override
    public void setAddress(String address) {
        mAddress = address;
    }
}