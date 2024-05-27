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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity{
    Toolbar toolbar;
    TextView subTotal, discount, deliveryFee, total;
    Button btnPayment;
    double amount = 0.0;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        
        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        amount  =  getIntent().getDoubleExtra("amount",0.0);
        
        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        deliveryFee = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);
        btnPayment = findViewById(R.id.pay_btn);
        
        subTotal.setText("Php" + amount);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentActivity.this,"Please pay in cash on delivery",Toast.LENGTH_LONG).show();
                startActivity(new Intent(PaymentActivity.this,MainActivity.class));
            }
        });

    }
}