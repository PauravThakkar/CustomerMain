package com.example.customermain;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {


    private ArrayList<FoodItems> list_members;
    FirebaseAuth mAuth;
    View view;
    ItemViewHolder holder;
    private Context context;
    String canteenId;

    public ItemAdapter(ArrayList<FoodItems> persons, Context context) {
        this.list_members = persons;
        this.context = context;
        mAuth =FirebaseAuth.getInstance();
        canteenId = mAuth.getUid();
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_menuitem, parent, false);
        holder=new ItemViewHolder(view);

        return holder;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final FoodItems list_items=list_members.get(position);

        holder.name.setText(list_items.getName());
        holder.price.setText(String.valueOf(list_items.getPrice()));
        if(list_items.getAvailability()) {
            holder.availability.setText("Sold Out");
            holder.availability.setBackgroundColor(context.getResources().getColor(R.color.soldOut));
            holder.availability.setBackground(context.getResources().getDrawable(R.drawable.rounded_holder_soldout));
        }

        else
        {
            holder.availability.setText("Available");
            holder.availability.setBackgroundColor(context.getResources().getColor(R.color.available));
            holder.availability.setBackground(context.getResources().getDrawable(R.drawable.rounded_holder_available));


        }
        holder.availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = list_items.getKey();
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("item").child(canteenId).child(id);
                if(list_items.getAvailability())
                {
                    mRef.child("availability").setValue(false);
                }
                else
                {
                    mRef.child("availability").setValue(true);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return list_members.size();
    }



    //View holder class, where all view components are defined
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public TextView name,price;
        public Button availability;
        public LinearLayout item_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);

            item_layout = itemView.findViewById(R.id.customMenu);
            name=itemView.findViewById(R.id.text_name);
            price=itemView.findViewById(R.id.text_price);
            availability=itemView.findViewById(R.id.btn_availability);

            item_layout.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(this.getAdapterPosition(),101,0,"Edit");
            contextMenu.add(this.getAdapterPosition(),102,1,"Delete");
        }




//        @Override
//        public void onClick(View v) {
//
//        }
    }
    public void removeItem(int position) {
        final FoodItems list_items=list_members.get(position);
        String id = list_items.getKey();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("item").child(canteenId).child(id);
        mRef.removeValue();

    }

    public void updateItem(int position)
    {
        final FoodItems list_items=list_members.get(position);
        final String id = list_items.getKey();
        String itemName = list_items.getName();
        int itemPrice = list_items.getPrice();
        final boolean itemAvail = list_items.getAvailability();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextUName);
        final EditText editTextPrice = dialogView.findViewById(R.id.editTextUPrice);
        Button update_btn = dialogView.findViewById(R.id.btn_uUpdate);
        Button cancle_btn = dialogView.findViewById(R.id.btn_uCancle);
        editTextName.setText(itemName);
        editTextPrice.setText(String.valueOf(itemPrice));

        final AlertDialog update = dialogBuilder.create();
        update.show();
        update.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                int price = Integer.parseInt(editTextPrice.getText().toString());
                DatabaseReference dr = FirebaseDatabase.getInstance().getReference("item").child(canteenId).child(id);
                FoodItems item = new FoodItems(id,name,price,itemAvail);
                dr.setValue(item);
                update.dismiss();

            }
        });

        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update.dismiss();
            }
        });
    }
}