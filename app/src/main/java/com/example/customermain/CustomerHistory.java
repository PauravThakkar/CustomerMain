package com.example.customermain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerHistory extends Fragment {
    RecyclerView historyRecyclerView;
    DatabaseReference mRef;
    ArrayList<OrderItemDetails> orderItems;
    HistoryAdapter historyAdapter;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_customer_history,container,false);
        historyRecyclerView = view.findViewById(R.id.order_history_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        historyRecyclerView.setLayoutManager(manager);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("order").child(mAuth.getUid());
        orderItems = new ArrayList<>();

        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderItems.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    OrderItemDetails item = ds.getValue(OrderItemDetails.class);
                    orderItems.add(item);
                }
                historyAdapter = new HistoryAdapter(getActivity(),orderItems);
                historyRecyclerView.setAdapter(historyAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
