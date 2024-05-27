package com.example.citu_crate;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ViewHolder> {
    private Context context;
    private List<PoplularProductModel> poplularProductModelList;

    public PopularProductAdapter(Context context, List<PoplularProductModel> poplularProductModelList) {
        this.context = context;
        this.poplularProductModelList = poplularProductModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(poplularProductModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(poplularProductModelList.get(position).getName());
        holder.price.setText(String.valueOf(poplularProductModelList.get(position).getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    Intent intent = new Intent(context, DetailedActivity.class);
                    intent.putExtra("detailed",poplularProductModelList.get(holder.getPosition()));
                    context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return poplularProductModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.all_img);
            name = itemView.findViewById(R.id.all_product_name);
            price = itemView.findViewById(R.id.all_price);
        }
    }
}
