package com.example.customermain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerMenu extends Fragment {
    RecyclerView menuRecyclerView;
    ItemAdapter menuAdapter;
    ArrayList<FoodItems> menuItems = new ArrayList<>();
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_manager_menu,container,false);

        mAuth = FirebaseAuth.getInstance();
        menuRecyclerView = view.findViewById(R.id.menuItemView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        menuRecyclerView.setLayoutManager(manager);
        // menuRecyclerView.setAdapter(menuAdapter);

        registerForContextMenu(menuRecyclerView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference("item").child(mAuth.getUid());

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) view.findViewById(R.id.floating_action_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View dialogView = inflater.inflate(R.layout.add_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText editTextName = dialogView.findViewById(R.id.editTextAName);
                final EditText editTextPrice = dialogView.findViewById(R.id.editTextAPrice);
                Button add_btn = dialogView.findViewById(R.id.btn_aAdd);
                Button cancle_btn = dialogView.findViewById(R.id.btn_aCancle);

                final AlertDialog add = dialogBuilder.create();
                add.show();
                add.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = editTextName.getText().toString();
                        int price = Integer.parseInt(String.valueOf(editTextPrice.getText()));
                        String id = mRef.push().getKey();
                        boolean availability = true;
                        FoodItems item = new FoodItems(id,name,price,availability);
                        mRef.child(id).setValue(item);
                        add.dismiss();
                    }
                });
                cancle_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add.dismiss();;
                    }
                });
            }
        });
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                menuItems.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FoodItems item = ds.getValue(FoodItems.class);
                    menuItems.add(item);
                }

                menuAdapter = new ItemAdapter(menuItems,getActivity());
                menuRecyclerView.setAdapter(menuAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
    public boolean onContextItemSelected(MenuItem item)
    {

        switch(item.getItemId()){
            case 101:
                menuAdapter.updateItem(item.getGroupId());
                return true;
            case 102:
                menuAdapter.removeItem(item.getGroupId());
                return true;
        }
        return false;
    }

}
