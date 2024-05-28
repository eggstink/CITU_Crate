package com.example.citu_crate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TopUpActivity extends AppCompatActivity {
    String[] payment = {"GCash", "Maya", "PayPal", "7-Eleven"};
    AutoCompleteTextView paymentDropDown;
    ArrayAdapter<String> adapterPayments;

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
//
//        paymentDropDown.setAdapter(adapterPayments);
//        paymentDropDown.setOnClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String item = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(TopUpActivity.this, "Item:")
//            }
//        });
    }
}