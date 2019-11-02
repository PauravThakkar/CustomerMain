package com.example.customermain;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class Tab_3 extends Fragment implements RequestedOrderAdapter.OnItemClickListener{

    RecyclerView completedRecyclerview;
    RequestedOrderAdapter completedOrderAdapter;
    DatabaseReference mCompleted;
    ArrayList<OrderItems> orders;
    String canteenId;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.tab_3,container,false);
        completedRecyclerview = view.findViewById(R.id.completed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        completedRecyclerview.setLayoutManager(layoutManager);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat orderStore = new SimpleDateFormat("yyyyMMdd");
        String storeKey = orderStore.format(cal.getTime());
        canteenId = FirebaseAuth.getInstance().getUid();
        orders = new ArrayList<>();
        mCompleted = FirebaseDatabase.getInstance().getReference("completedOrder").child(canteenId).child(storeKey);

        mCompleted.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    OrderItems order = ds.getValue(OrderItems.class);
                    orders.add(order);
                }
                completedOrderAdapter = new RequestedOrderAdapter(orders);
                completedRecyclerview.setAdapter(completedOrderAdapter);
                completedOrderAdapter.setOnItemClickListener(Tab_3.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
    public void onItemClick(int position){
        Intent i =new Intent(this.getActivity(),OrderDetails.class);
        OrderItems item= orders.get(position);

        i.putExtra("order", item.getOrderKey());
        i.putExtra("id","completedOrder");
        i.putExtra("canteen",canteenId);
        startActivity(i);
    }
}
