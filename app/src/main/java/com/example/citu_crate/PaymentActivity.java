package com.example.citu_crate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity{
    Toolbar tolbar;
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
        
x        amount  =  getIntent().getDoubleExtra("amount",0.0);
        
        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        deliveryFee = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);
        btnPayment = findViewById(R.id.pay_btn)
        
        subTotal.setText("Php" + amount);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentActivity.this,"Please pay in cash on delivery",Toast.LENGTH_LONG);
            }
        });
        
        //detailactivity buyNow Intent intent = new Intent(DetailActivity.this, PaymentActivity.class);
            if(newProductsModel != null){
                intent.putExtra("item", newProductsModel);
            }
            if(popularProductsModel != null){
                intent.putExtra("item", popularProductsModel);
            }
        if(showAllModel != null){
            intent.putExtra("item", showAllModel);
        }
        //startActivity(intent);
        
        //AddressActivity onCreate Object obj = getIntent().getSerializableEExtra("item);
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
        //2.00 mark payment 2.0
    }
}