package com.example.customermain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CanteenMenu extends AppCompatActivity {
    RecyclerView menuRecyclerView;
    MenuAdapterC menuAdapter;
    ArrayList<FoodItems> menuItems = new ArrayList<>();
    ArrayList<String> orderItems = new ArrayList<>();
    DatabaseReference mRefItem, mRefCanteen;
    Canteens canteen;
    TextView canteenTitle;
    ImageView canteenImg;
    CardView cart;
    ImageButton imgButtoncall;
    ImageButton imgButtonlocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_menu);

        imgButtoncall =(ImageButton)findViewById(R.id.imageButtoncall);
        imgButtoncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"You download is resumed",Toast.LENGTH_LONG).show();
            }
        });
        imgButtonlocation =(ImageButton)findViewById(R.id.imageButtonlocation);
        imgButtonlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"You download is resumed",Toast.LENGTH_LONG).show();
            }
        });

        RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar); // initiate a rating bar
        Float ratingNumber = (float)3.5 ;
        simpleRatingBar.setRating(ratingNumber);



        final Intent i = getIntent();
        final String canteenId = i.getStringExtra("canteen");
        Log.d("key",canteenId);
        final int canteenPos = i.getIntExtra("position",0);

        canteenTitle = findViewById(R.id.txt_canteenTitle);
        canteenImg = findViewById(R.id.img_canteen);
        cart = findViewById(R.id.test);

        menuRecyclerView = findViewById(R.id.menuItemView);
        LinearLayoutManager manager = new LinearLayoutManager(CanteenMenu.this);
        menuRecyclerView.setLayoutManager(manager);

        mRefItem = FirebaseDatabase.getInstance().getReference("item").child(canteenId);
        mRefCanteen = FirebaseDatabase.getInstance().getReference("canteen").child(canteenId);

        mRefCanteen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                canteen = dataSnapshot.getValue(Canteens.class);
                Log.d("Canteen",canteen.toString());
                canteenTitle.setText(canteen.getName());
                Picasso.with(CanteenMenu.this)
                        .load(canteen.getImage())
                        .fit()
                        .centerCrop()
                        .transform(new CropCircleTransformation())
                        .into(canteenImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        orderItems = menuAdapter.order_members;
                        Intent i = new Intent(CanteenMenu.this,CustomeMain.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("cartItems",menuAdapter.order_members);
                        bundle.putString("canteen", canteen.getKey());
                        bundle.putInt("position",canteenPos);
                        i.putExtras(bundle);
                        startActivity(i);



            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mRefItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menuItems.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FoodItems item = ds.getValue(FoodItems.class);
                    if(item.getAvailability())
                    {
                        menuItems.add(item);
                    }
                }

                menuAdapter = new MenuAdapterC(CanteenMenu.this, menuItems);
                menuRecyclerView.setAdapter(menuAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}