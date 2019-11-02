package com.example.customermain;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class Tab_1 extends Fragment implements OngoingOrderAdapter.OnItemClickListener {

    RecyclerView ongoingRecyclerview;
    OngoingOrderAdapter ongoingOrderAdapter;
    DatabaseReference mOngoing;
    ArrayList<OrderItems> orders;
    private Paint p = new Paint();
    String canteenId;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.tab_1,container,false);
        ongoingRecyclerview = view.findViewById(R.id.ongoing);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ongoingRecyclerview.setLayoutManager(layoutManager);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat orderStore = new SimpleDateFormat("yyyyMMdd");
        String storeKey = orderStore.format(cal.getTime());
        canteenId = FirebaseAuth.getInstance().getUid();
        orders = new ArrayList<>();
        mOngoing = FirebaseDatabase.getInstance().getReference("ongoingOrder").child(canteenId).child(storeKey);

        mOngoing.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    OrderItems order = ds.getValue(OrderItems.class);
                    orders.add(order);
                }
                ongoingOrderAdapter = new OngoingOrderAdapter(orders);
                ongoingRecyclerview.setAdapter(ongoingOrderAdapter);
                ongoingOrderAdapter.setOnItemClickListener(Tab_1.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        initSwipe();
        return view;
    }
    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT){
                    ongoingOrderAdapter.removeItem(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.completed);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(ongoingRecyclerview);
    }
    public void onItemClick(int position){
        Intent i =new Intent(this.getActivity(),OrderDetails.class);
        OrderItems item= orders.get(position);
        i.putExtra("order", item.getOrderKey());
        i.putExtra("id","ongoingOrder");

        startActivity(i);
    }
}
