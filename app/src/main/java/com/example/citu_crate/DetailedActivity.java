package com.example.citu_crate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailedActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageView detailedImg;
    TextView rating, name, description, price;
    Button addToCart, buyNow;
    ImageView addItems, removeItems;
    NewProductsModel newProductsModel = null;
    PoplularProductModel poplularProductModel = null;
    ShowAllModel showAllModel = null;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.detailed_toolbar);
        setActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //2:41 toolbar 2

        firestore = FirebaseFirestore.getInstance();

        final Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof NewProductsModel) {
            newProductsModel = (NewProductsModel) obj;
        } else if (obj instanceof PoplularProductModel) {
            poplularProductModel = (PoplularProductModel) obj;
        }else if (obj instanceof ShowAllModel) {
            showAllModel = (ShowAllModel) obj;
        }


        detailedImg = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        rating = findViewById(R.id.rating);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);

        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItems = findViewById(R.id.detailed_img);
        removeItems = findViewById(R.id.detailed_img);

        if (newProductsModel != null) {
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            name.setText(newProductsModel.getName());
            rating.setText(newProductsModel.getRating());
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            name.setText(newProductsModel.getName());
        }

        if (poplularProductModel != null) {
            Glide.with(getApplicationContext()).load(poplularProductModel.getImg_url()).into(detailedImg);
            name.setText(poplularProductModel.getName());
            rating.setText(poplularProductModel.getRating());
            description.setText(poplularProductModel.getDescription());
            price.setText(String.valueOf(poplularProductModel.getPrice()));
            name.setText(poplularProductModel.getName());
        }
        if (showAllModel != null) {
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());
        }

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this, PaymentActivity.class);
                if (newProductsModel != null) {
                    intent.putExtra("item", newProductsModel);
                }
                if (poplularProductModel != null) {
                    intent.putExtra("item", poplularProductModel);
                }
                if (showAllModel != null) {
                    intent.putExtra("item", showAllModel);
                }
                startActivity(intent);
            }
        });

    }
}