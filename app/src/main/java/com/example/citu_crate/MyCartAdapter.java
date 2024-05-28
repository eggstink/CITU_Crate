package com.example.citu_crate;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {
    private Context context;
    private List<MyCartModel> cartModelList;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public MyCartAdapter(Context context, List<MyCartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyCartModel cartModel = cartModelList.get(position);
        holder.productName.setText(cartModel.getProductName());
        holder.productPrice.setText("₱" + cartModel.getProductPrice());
        holder.quantity.setText(cartModel.getTotalQuantity());
        holder.totalPrice.setText("₱" + (Integer.parseInt(cartModel.getProductPrice()) * Integer.parseInt(cartModel.getTotalQuantity())));
        holder.currentDate.setText(cartModel.getCurrentDate());
        holder.currentTime.setText(cartModel.getCurrentTime());

        holder.addItems.setOnClickListener(v -> {
            int newQuantity = Integer.parseInt(holder.quantity.getText().toString()) + 1;
            holder.quantity.setText(String.valueOf(newQuantity));
            updateQuantity(cartModel, newQuantity);
        });

        holder.removeItems.setOnClickListener(v -> {
            int newQuantity = Integer.parseInt(holder.quantity.getText().toString()) - 1;
            if (newQuantity >= 0) {
                holder.quantity.setText(String.valueOf(newQuantity));
                updateQuantity(cartModel, newQuantity);
            } else {
                Toast.makeText(context, "Quantity cannot be less than zero", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, quantity, totalPrice, currentDate, currentTime;
        ImageView addItems, removeItems;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.total_price);
            currentDate = itemView.findViewById(R.id.current_date);
            currentTime = itemView.findViewById(R.id.current_time);
            addItems = itemView.findViewById(R.id.add_item);
            removeItems = itemView.findViewById(R.id.remove_item);
        }
    }

    private void updateQuantity(MyCartModel cartModel, int newQuantity) {
        if (newQuantity == 0) {
            removeItem(cartModel);
        } else {
            cartModel.setTotalQuantity(String.valueOf(newQuantity));
            int totalPrice = Integer.parseInt(cartModel.getProductPrice()) * newQuantity;
            cartModel.setTotalPrice(totalPrice);

            firestore.collection("AddToCart")
                    .document(auth.getCurrentUser().getUid())
                    .collection("User")
                    .document(cartModel.getCartItemId())
                    .update("totalQuantity", String.valueOf(newQuantity),
                            "totalPrice", totalPrice)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            updateTotalAmount();
                        } else {
                            Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void removeItem(MyCartModel cartModel) {
        firestore.collection("AddToCart")
                .document(auth.getCurrentUser().getUid())
                .collection("User")
                .document(cartModel.getCartItemId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cartModelList.remove(cartModel);
                        notifyDataSetChanged();
                        updateTotalAmount();
                    } else {
                        Toast.makeText(context, "Failed to remove item from cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateTotalAmount() {
        int totalAmount = 0;
        for (MyCartModel cartModel : cartModelList) {
            totalAmount += cartModel.getTotalPrice();
        }
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount", totalAmount);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}