package com.example.customermain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class OrderDetails extends AppCompatActivity {
    private OrderItems item;
    private DatabaseReference mRef,mRefItem;
    private RecyclerView orderitems;
    private TextView orderNum,orderPrice,orderQuantity,orderTime;
    private ArrayList<FoodItems> items = new ArrayList<>();
    private ArrayList<Integer> itemQuantity;
    private ArrayList<String> itemKey;
    private OrderDetailsAdapter orderDetailsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Intent i = getIntent();
        String key = i.getStringExtra("order");
        String table = i.getStringExtra("id");
        String canteen = FirebaseAuth.getInstance().getUid();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat orderStore = new SimpleDateFormat("yyyyMMdd");
        String storeKey = orderStore.format(cal.getTime());

        orderNum = findViewById(R.id.txt_orderNum);
        orderPrice = findViewById(R.id.txt_totalPrice);
        orderQuantity = findViewById(R.id.txt_totalItems);
        orderitems = findViewById(R.id.orderitems);
        orderTime = findViewById(R.id.txt_orderTime);
        LinearLayoutManager manager = new LinearLayoutManager(OrderDetails.this);
        orderitems.setLayoutManager(manager);

        mRef = FirebaseDatabase.getInstance().getReference(table).child(canteen).child(storeKey).child(key);
        mRefItem = FirebaseDatabase.getInstance().getReference("item").child(canteen);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!= null){
                    item = dataSnapshot.getValue(OrderItems.class);
                    orderNum.setText(item.getOrderNum());
                    orderPrice.setText(item.getOrderPrice());
                    orderQuantity.setText(item.getOrderItems());
                    orderTime.setText(item.getOrderTime());
                    itemQuantity = item.getOrderItemCount();
                    itemKey = item.getOrderItemList();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mRefItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               items.clear();
                for(String i:itemKey){
                    FoodItems item = dataSnapshot.child(i).getValue(FoodItems.class);
                    items.add(item);
                }
                Log.d("foodItems",items.toString());
                orderDetailsAdapter = new OrderDetailsAdapter(OrderDetails.this,items,itemQuantity);
                orderitems.setAdapter(orderDetailsAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
