package com.example.customermain;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuAdapterC extends RecyclerView.Adapter<MenuAdapterC.ItemViewHolder> {
    private ArrayList<FoodItems> list_members;
    public ArrayList<String> order_members = new ArrayList<>();
    View view;
    ItemViewHolder holder;
    private Context context;
    CardView cart;
    TextView cartItems;
    TextView cartPrice;
    int sum = 0;


    public MenuAdapterC(final Context context, ArrayList<FoodItems> menuItems) {
        this.list_members = menuItems;
        this.context = context;
        cart = ((Activity) context).findViewById(R.id.test);
        cartItems = ((Activity) context).findViewById(R.id.txt_itemCount);
        cartPrice = ((Activity) context).findViewById(R.id.txt_itemPrice);

    }

    @Override
    public MenuAdapterC.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_menu_itemc, parent, false);

        holder = new MenuAdapterC.ItemViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MenuAdapterC.ItemViewHolder holder, int position) {
        final FoodItems list_items = list_members.get(position);
        if (list_items.getAvailability()) {
            holder.name.setText(list_items.getName());
            holder.price.setText(String.valueOf(list_items.getPrice()));

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("btn", String.valueOf(order_members.size()));
                    if (holder.add.getText().toString().equals("Add")) {

                        if (cart.getVisibility() == View.GONE) {
                            cart.setVisibility(View.VISIBLE);
                            order_members.add(list_items.getKey());
                            holder.add.setText("Remove");
                            String itemCount = String.valueOf(order_members.size());
                            cartItems.setText(itemCount + " Items");
                            sum += list_items.getPrice();
                            cartPrice.setText(String.valueOf(sum));

                        } else {

                            holder.add.setText("Remove");
                            order_members.add(list_items.getKey());
                            String itemCount = String.valueOf(order_members.size());
                            cartItems.setText(itemCount + " Items");
                            sum += list_items.getPrice();
                            cartPrice.setText(String.valueOf(sum));
                        }
                    } else {

                        order_members.remove(list_items.getKey());
                        holder.add.setText("Add");
                        String itemCount = String.valueOf(order_members.size());
                        cartItems.setText(itemCount + " Items");
                        sum -= list_items.getPrice();
                        cartPrice.setText(String.valueOf(sum));
                        if (order_members.size() == 0) {
                            cart.setVisibility(View.GONE);
                        }
                    }

                }
            });

        }


    }


    @Override
    public int getItemCount() {
        return list_members.size();
    }


    //View holder class, where all view components are defined
    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView name, price;
        public Button add;
        //  public LinearLayout item_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);

            //  item_layout = itemView.findViewById(R.id.customMenuC);
            name = itemView.findViewById(R.id.text_nameC);
            price = itemView.findViewById(R.id.text_priceC);
            add = itemView.findViewById(R.id.btn_additem);

        }


    }
}
