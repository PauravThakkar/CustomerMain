package com.example.customermain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerCart extends Fragment {
    RecyclerView cartRecyclerView;
    private ArrayList<String> cartItemsKey = new ArrayList<>();
    DatabaseReference mRefItem, mRefCanteen, mRefCart, mRefRequested, mRefOngoing, mRefCompleted;
    ArrayList<FoodItems> cartItems = new ArrayList<>();
    CartAdapter cartAdapter;
    TextView canteenTitle;
    ImageView canteenImg;
    Button order;
    int orderCount = 0;
    FirebaseAuth mAuth;
    Canteens canteen;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (this.getArguments() != null) {
            view = inflater.inflate(R.layout.activity_customer_cart, container, false);

            cartItemsKey = this.getArguments().getStringArrayList("cartItems");
            final String canteenId = this.getArguments().getString("canteen");
            final int canteenPos = this.getArguments().getInt("position", 0);
            mAuth = FirebaseAuth.getInstance();
            cartRecyclerView = view.findViewById(R.id.cartView);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            cartRecyclerView.setLayoutManager(manager);

            canteenTitle = view.findViewById(R.id.cartName);
            canteenImg = view.findViewById(R.id.cartImage);
            order = view.findViewById(R.id.btn_order);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat orderStore = new SimpleDateFormat("yyyyMMdd");
            String storeKey = orderStore.format(cal.getTime());

            mRefItem = FirebaseDatabase.getInstance().getReference("item").child(canteenId);
            mRefCanteen = FirebaseDatabase.getInstance().getReference("canteen").child(canteenId);
            mRefCart = FirebaseDatabase.getInstance().getReference("order").child(mAuth.getUid());
            mRefRequested = FirebaseDatabase.getInstance().getReference("requestedOrder").child(canteenId).child(storeKey);
            mRefOngoing = FirebaseDatabase.getInstance().getReference("ongoingOrder").child(canteenId).child(storeKey);
            mRefCompleted = FirebaseDatabase.getInstance().getReference("completedOrder").child(canteenId).child(storeKey);


            mRefCanteen.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   canteen = dataSnapshot.getValue(Canteens.class);
                    canteenTitle.setText(canteen.getName());
                    Picasso.with(getActivity())
                            .load(canteen.getImage())
                            .fit()
                            .transform(new CropCircleTransformation())
                            .into(canteenImg);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = (int) dataSnapshot.getChildrenCount();
                    orderCount += count;
                    Log.d("TAG", "count= " + dataSnapshot.getChildrenCount());

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mRefRequested.addValueEventListener(valueEventListener);
            mRefCompleted.addValueEventListener(valueEventListener);
            mRefOngoing.addValueEventListener(valueEventListener);


            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy 'at' hh:mm a");
                    SimpleDateFormat orderFormat = new SimpleDateFormat("yyyyMMdd");
                    String currentTime = dateFormat.format(cal.getTime());
                    String orderNo = String.format("%02d", canteenPos + 1);
                    orderNo += String.format("%04d", orderCount + 1);
                    String key = mRefCart.push().getKey();
                    OrderItems item = new OrderItems(key, orderNo,mAuth.getUid(), currentTime, cartItemsKey, cartAdapter.item_count, canteenId, String.valueOf(cartAdapter.totalprice), String.valueOf(cartAdapter.totalitem));
                    mRefCart.child(key).setValue(item);
                    mRefRequested.child(key).setValue(item);
                    Toast toast = Toast.makeText(getActivity(), "your Order number is" + orderNo, Toast.LENGTH_SHORT);
                    toast.show();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://fir-demo-55b0b.firebaseapp.com/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Api api =retrofit.create(Api.class);
                    Call<ResponseBody> call = api.sendNotification(canteen.getToken(),"New Order", "You have a new Order... ");
                    Log.d("Token Canteen",canteen.getToken());
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
            });
            mRefItem.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cartItems.clear();
                    for (String i : cartItemsKey) {
                        FoodItems item = dataSnapshot.child(i).getValue(FoodItems.class);
                        cartItems.add(item);
                    }

                    cartAdapter = new CartAdapter(getActivity(), cartItems);
                    cartRecyclerView.setAdapter(cartAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Log.d("Presence in if","I am in if alos");

        }
        else {
            Log.d("RR","I am in else");
            view = inflater.inflate(R.layout.activity_canteen_emptycart, container, false);

        }
        return view;
    }
}

