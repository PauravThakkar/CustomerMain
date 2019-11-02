package com.example.customermain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ItemViewHolder>{
    private Context context;
    private ArrayList<FoodItems> items;
    private ArrayList<Integer> itemQuantity;
    private ItemViewHolder holder;
    public OrderDetailsAdapter(Context context, ArrayList<FoodItems> items,ArrayList<Integer> itemQuantity)
    {
        this.context = context;
        this.items = items;
        this.itemQuantity = itemQuantity;
    }
    @Override
    public OrderDetailsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_orderdetails, parent, false);
        holder = new ItemViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderDetailsAdapter.ItemViewHolder holder, int position) {
        holder.itemName.setText(items.get(position).getName());
        int Price,Quantity;
        Price = items.get(position).getPrice();
        Quantity = itemQuantity.get(position);
        holder.itemPrice.setText(String.valueOf(Price));
        holder.itemQuantity.setText(String.valueOf(Quantity));
        holder.itemTotal.setText(String.valueOf(Price*Quantity));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder  {
        public TextView itemName,itemPrice,itemQuantity,itemTotal;


        public ItemViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.txt_orderItem);
            itemPrice = itemView.findViewById(R.id.txt_orderPrice);
            itemQuantity = itemView.findViewById(R.id.txt_orderQuantity);
            itemTotal = itemView.findViewById(R.id.txt_orderItemTotal);
        }

    }
}
