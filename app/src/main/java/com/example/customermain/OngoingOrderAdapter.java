package com.example.customermain;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OngoingOrderAdapter extends RecyclerView.Adapter<OngoingOrderAdapter.ItemViewHolder> {

    private ArrayList<OrderItems> orderItemList;
    private ItemViewHolder holder;
    private OngoingOrderAdapter.OnItemClickListener mListener;
    private DatabaseReference mOngoing,mCompleted,mRefUsers;
    private String canteenId;

    public OngoingOrderAdapter(ArrayList<OrderItems> orderItemList) {

        this.orderItemList = orderItemList;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat orderStore = new SimpleDateFormat("yyyyMMdd");
        canteenId = FirebaseAuth.getInstance().getUid();
        String storeKey = orderStore.format(cal.getTime());
        mOngoing = FirebaseDatabase.getInstance().getReference("ongoingOrder").child(canteenId).child(storeKey);
        mCompleted= FirebaseDatabase.getInstance().getReference("completedOrder").child(canteenId).child(storeKey);
        mRefUsers = FirebaseDatabase.getInstance().getReference("Users");
    }

    @Override
    public OngoingOrderAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cutom_orderview, parent, false);
        holder = new OngoingOrderAdapter.ItemViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(OngoingOrderAdapter.ItemViewHolder holder, int position) {
        holder.orderNum.setText(orderItemList.get(position).getOrderNum());
        holder.orderPrice.setText(orderItemList.get(position).getOrderPrice());

    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView orderNum,orderPrice;


        public ItemViewHolder(View itemView) {
            super(itemView);
            orderNum = itemView.findViewById(R.id.txt_orderNum);
            orderPrice = itemView.findViewById(R.id.txt_orderPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            mListener.onItemClick(position);

        }
    }
    public void removeItem(int position){
        final OrderItems item = orderItemList.get(position);
        String key = item.getOrderKey();
        mCompleted.child(key).setValue(item);
        mOngoing.child(key).removeValue();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fir-demo-55b0b.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Api api =retrofit.create(Api.class);
        mRefUsers.child(item.getOrderUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Call<ResponseBody> call = api.sendNotification(user.getToken(),"Order", "Your Order "+item.getOrderNum()+" is Ready");

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("notify","send "+response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //add notify user

    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }
    public void setOnItemClickListener(OngoingOrderAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
