package com.example.customermain;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CanteenMenu extends AppCompatActivity {
    RecyclerView menuRecyclerView;
    MenuAdapterC menuAdapter;
    ArrayList<FoodItems> menuItems = new ArrayList<>();
    ArrayList<String> orderItems = new ArrayList<>();
    DatabaseReference mRefItem, mRefCanteen;
    Canteens canteen;
    String number = "9586666972";
    float latitude, longitude;
    TextView canteenTitle;
    ImageView canteenImg;
    CardView cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_menu);
        final Intent i = getIntent();
        final String canteenId = i.getStringExtra("canteen");
        Log.d("key", canteenId);
        final int canteenPos = i.getIntExtra("position", 0);

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
                Log.d("Canteen", canteen.toString());
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
                Intent i = new Intent(CanteenMenu.this, CustomeMain.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("cartItems", menuAdapter.order_members);
                bundle.putString("canteen", canteen.getKey());
                bundle.putInt("position", canteenPos);
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
                    if (item.getAvailability()) {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void call() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));//change the number
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }
    private void location()
    {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

}