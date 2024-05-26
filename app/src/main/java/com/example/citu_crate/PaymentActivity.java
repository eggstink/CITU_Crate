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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener{
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
        
x        amount  =  getIntent().getDoubleExtra("amount",0.0)
        
        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        deliveryFee = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);
        btnPayment = findViewById(R.id.pay_btn)
        
        subTotal.setText("Php" + amount);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod();
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

    private void paymentMethod() {
        Checkout checkout = new Checkout();

        final Activity activity = PaymentActivity.this;

        try {
            JSONObject options = new JSONObject();
            //Set Company Name
            options.put("name", "My E-Commerce App");
            //Ref no
            options.put("description", "Reference No. #123456");
            //Image to be display
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            // Currency type
            options.put("currency", "USD");
            //double total = Double.parseDouble(mAmountText.getText().toString());
            //multiply with 100 to get exact amount in rupee
            amount = amount * 100;
            //amount
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            //email
            preFill.put("email", "developer.kharag@gmail.com");
            //contact
            preFill.put("contact", "7489347378");

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
    }
}