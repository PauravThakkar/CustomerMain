package com.example.customermain;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemViewHolder> {


    private ArrayList<FoodItems> list_members;
    ArrayList<Integer> item_count=new ArrayList<>();
    View view;
    ItemViewHolder holder;
    private Context context;
    int totalprice = 0;
    int totalitem = 0;
    TextView price,pay;

    public CartAdapter(Context context, ArrayList<FoodItems> persons) {
        this.list_members = persons;
        this.context = context;


        for(int j = 0;j<list_members.size();j++)
        {
            this.item_count.add(1);
            this.totalprice += list_members.get(j).getPrice();
            totalitem+=1;
        }
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cartitem, parent, false);
        price = ((Activity)parent.getContext()).findViewById(R.id.cartPrice);
        pay = ((Activity)parent.getContext()).findViewById(R.id.cartPay);
        holder=new ItemViewHolder(view);

        setPrice();
        return holder;
    }

    public void setPrice()
    {
        price.setText(String.valueOf(totalprice));
        pay.setText(String.valueOf(totalprice));
    }
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final FoodItems list_items=list_members.get(position);
        holder.name.setText(list_items.getName());
        holder.price.setText(String.valueOf(list_items.getPrice()));
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalprice -= (list_members.get(position).getPrice()*item_count.get(position));
                totalitem -= item_count.get(position);
                setPrice();
                list_members.remove(position);
                item_count.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_count.set(position, item_count.get(position) + 1);
                totalitem+=1;
                totalprice += list_members.get(position).getPrice();
                setPrice();
                holder.itemAmount.setText(String.valueOf(item_count.get(position)));
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalprice -= list_members.get(position).getPrice();
                totalitem-=1;
                setPrice();
                item_count.set(position, item_count.get(position) - 1);
                if(item_count.get(position)==0)
                {
                    list_members.remove(position);
                    item_count.remove(position);
                    notifyDataSetChanged();
                }
                else
                {
                    holder.itemAmount.setText(String.valueOf(item_count.get(position)));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_members.size();
    }

    public
    //View holder class, where all view components are defined
    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView name, price;
        public TextView itemAmount;
        ImageView plus,minus,remove;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_name);
            price = itemView.findViewById(R.id.txt_price);
            itemAmount = itemView.findViewById(R.id.txt_itemAmount);
            plus = itemView.findViewById(R.id.increase);
            remove = itemView.findViewById(R.id.btn_availability);
            minus = itemView.findViewById(R.id.decrease);
        }
    }
}