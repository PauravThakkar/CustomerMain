package com.example.customermain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class CanteenAdapter extends RecyclerView.Adapter<CanteenAdapter.ImageViewHolder> {
    private Context mContext;
    Context cContext;
    private List<Canteens> mUploads;
    CanteenAdapter.ImageViewHolder holder;
    private OnItemClickListener mListener;

    public CanteenAdapter(Context context, List<Canteens> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        cContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_canteen, parent, false);
        holder = new ImageViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Canteens mcanteen = mUploads.get(position);
        holder.canteenName.setText(mcanteen.getName());
        Picasso.with(cContext)
                .load(mcanteen.getImage())
                .fit()
                .centerCrop()
                .into(holder.canteenImg);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView canteenName;
        public ImageView canteenImg;

        public ImageViewHolder(View itemView) {
            super(itemView);
            canteenName = itemView.findViewById(R.id.canteen_name);
            canteenImg = itemView.findViewById(R.id.img_canteen);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            mListener.onItemClick(position);

        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(CanteenAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}

