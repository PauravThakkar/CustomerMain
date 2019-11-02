package com.example.customermain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ItemViewHolder>{
    private Context context;
    private ArrayList<FoodItems> items;
    private ArrayList<Integer> itemQuantity;
    private HistoryItemAdapter.ItemViewHolder holder;

    View view;
    public HistoryItemAdapter(Context context, ArrayList<FoodItems> items,ArrayList<Integer> itemQuantity)
    {
        this.context = context;
        this.itemQuantity = itemQuantity;
        this.items = items;


    }
    @Override
    public HistoryItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cutom_history_items, parent, false);
        holder =new HistoryItemAdapter.ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryItemAdapter.ItemViewHolder holder, int position) {
        holder.itemName.setText(items.get(position).getName());
        holder.itemQuantity.setText(String.valueOf(itemQuantity.get(position)));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder  {
        public TextView itemName,itemQuantity;


        public ItemViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.txt_historyitemname);
            itemQuantity = itemView.findViewById(R.id.txt_historyitemquant);
        }

    }
}
