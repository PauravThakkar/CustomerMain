package com.example.customermain;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ItemViewHolder> {

    Context context;
    ArrayList<OrderItemDetails> items;
    View view;
    ItemViewHolder holder;
    DatabaseReference mRefCanteen;
    HistoryItemAdapter historyItemAdapter;

    ArrayList<String> itemKey;
    ArrayList<Integer> itemQuant;
    DatabaseReference mRefItem;
    public HistoryAdapter(Context context,ArrayList<OrderItemDetails> items) {
        this.context = context;
        this.items = items;

    }
    public HistoryAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_history, parent, false);
        holder=new HistoryAdapter.ItemViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final HistoryAdapter.ItemViewHolder holder, int position) {
        OrderItemDetails item = items.get(position);
        mRefCanteen = FirebaseDatabase.getInstance().getReference("canteen").child(item.orderCanteenId);

        itemKey = item.getOrderItemList();
        holder.keys=itemKey;
        itemQuant = item.getOrderItemCount();
        holder.quant=itemQuant;
        mRefCanteen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Canteens canteen = dataSnapshot.getValue(Canteens.class);
                holder.canteenTitle.setText(canteen.getName());
                Picasso.with(context)
                        .load(canteen.getImage())
                        .fit()
                        .transform(new CropCircleTransformation())
                        .into(holder.canteenImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRefItem = FirebaseDatabase.getInstance().getReference("item").child(item.getOrderCanteenId());
        mRefItem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.itemDetails.clear();

                for(String i:holder.keys){
                    FoodItems item = dataSnapshot.child(i).getValue(FoodItems.class);
                    holder.itemDetails.add(item);
                }
                Log.d("History",itemQuant.toString());
                historyItemAdapter = new HistoryItemAdapter(context,holder.itemDetails,holder.quant);
                holder.itemDetailsView.setAdapter(historyItemAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("ordernum",item.getOrderNum());
        holder.orderTime.setText(item.getOrderTime());
        holder.orderAmount.setText(item.getorderPrice());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView canteenTitle,orderTime,orderAmount;
        ImageView canteenImg;
        RecyclerView itemDetailsView;
        ArrayList<String> keys;
        ArrayList<Integer> quant;
        ArrayList<FoodItems> itemDetails;

        public ItemViewHolder(View itemView) {
            super(itemView);
            canteenTitle = itemView.findViewById(R.id.txt_canteentitle);
            canteenImg = itemView.findViewById(R.id.img_canteenimage);
            itemDetailsView = itemView.findViewById(R.id.historyitemview);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            itemDetailsView.setLayoutManager(manager);
            orderTime = itemView.findViewById(R.id.txt_ordertime);
            orderAmount = itemView.findViewById(R.id.txt_orderprice);
            itemDetails = new ArrayList<>();
        }
    }
}
