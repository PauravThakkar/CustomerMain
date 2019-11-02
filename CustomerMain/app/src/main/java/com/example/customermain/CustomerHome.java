package com.example.customermain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerHome extends Fragment implements CanteenAdapter.OnItemClickListener {
    private CardView menu_item;
    private RecyclerView mRecyclerView;
    private List<Canteens> mCanteens;
    CanteenAdapter mAdapter;
    DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_customer_home,container,false);

        mRef = FirebaseDatabase.getInstance().getReference("canteen");

        mRecyclerView = view.findViewById(R.id.menuItemView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCanteens = new ArrayList<>();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Canteens canteen = postSnapshot.getValue(Canteens.class);
                    mCanteens.add(canteen);
                }

                mAdapter = new CanteenAdapter(getActivity(),mCanteens);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(CustomerHome.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    public void onItemClick(int position){
        Intent i =new Intent(getActivity(),CanteenMenu.class);
        Canteens canteen = mCanteens.get(position);
        i.putExtra("canteen", canteen.getKey());
        i.putExtra("position",position);
        startActivity(i);
    }

}
