package com.example.citu_crate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress{

    EditText build, roomnum, note, phoneNum;
    Toolbar toolbar;
    Button btnAddAddress;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        build = findViewById(R.id.ad_build);
        roomnum = findViewById(R.id.ad_roomnum);
        note = findViewById(R.id.ad_note);
        phoneNum = findViewById(R.id.ad_phone);
        btnAddAddress = findViewById(R.id.ad_add_address);
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String building = build.getText().toString();
                String Roomnum = roomnum.getText().toString();
                String Note = note.getText().toString();
                String num = phoneNum.getText().toString();

                String final_address = "";

                if(!building.isEmpty()){
                    final_address+="Building: " + building;
                }
                if(!Roomnum.isEmpty()){
                    final_address+="\nRoom Number: " + Roomnum;
                }
                if(!Note.isEmpty()){
                    final_address+="\nNote: " + Note;
                }
                if(!num.isEmpty()){
                    if(num.length() != 11){
                        Toast.makeText(AddAddressActivity.this, "Phone Number should be 11 characters",Toast.LENGTH_SHORT).show();
                    }
                    final_address+="\nPhone Number: " + num;
                }
                if(!building.isEmpty() && !Roomnum.isEmpty() && !Note.isEmpty() && !num.isEmpty() ){
                    if(num.length()==11) {
                        Map<String, String> map = new HashMap<>();
                        map.put("userAddress", final_address);

                        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                                .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AddAddressActivity.this, "Address Added", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AddAddressActivity.this, AddressActivity.class));
                                            finish();
                                        }
                                    }
                                });
                    }
                }else{
                    Toast.makeText(AddAddressActivity.this, "Kindly Fill All Field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void setAddress(String address) {

    }
}