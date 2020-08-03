package com.example.inventaristoko.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.inventaristoko.Model.Food;
import com.example.inventaristoko.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Context context;
    List<Food> foodList;

    public RecyclerAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        holder.id.setText(foodList.get(position).getId());
        holder.name.setText(foodList.get(position).getName());
        holder.category.setText(foodList.get(position).getCategory());
        holder.price.setText(foodList.get(position).getPrice().toString());

        Glide.with(context).load(foodList.get(position).getId()).apply(RequestOptions.centerCropTransform()).into(holder.imageUrl);
    }

    @Override
    public int getItemCount() {
        if(foodList != null) {
            return foodList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        TextView category;
        TextView price;
        ImageView imageUrl;

        public MyViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.id);
            name = (TextView) itemView.findViewById(R.id.name);
            category = (TextView) itemView.findViewById(R.id.category);
            price = (TextView) itemView.findViewById(R.id.price);
            imageUrl = (ImageView) itemView.findViewById(R.id.imageUrl);
        }
    }
}
