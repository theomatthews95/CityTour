package com.example.admin123.citytour.Fragments.Favourites;

import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin123.citytour.DbBitmapUtility;
import com.example.admin123.citytour.R;

import java.util.Collections;
import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags;
import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by theom on 15/03/2017.
 */

public class FavouriteListAdapter extends SelectableAdapter<FavouriteListAdapter.MyViewHolder> {

    private List<FavouriteListItem> data = Collections.emptyList();
    private SparseBooleanArray selectedItems;

    public FavouriteListAdapter(List<FavouriteListItem> data){
        this.data=data;

        Log.i("Favourite_List_Adapter","Hi constructor");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View v = inflater.inflate(R.layout.favourite_list_item, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FavouriteListItem current = data.get(position);
        holder.title.setText(current.title);
        holder.locationPhoto.setImageBitmap(current.locationPhoto);
        Log.i("location photo", current.locationPhoto.toString());
        holder.setParams(current.reference, current.latitude, current.longitude);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        CardView cv;
        TextView title;
        ImageView locationPhoto;
        String reference;
        Double latitude;
        Double longitude;

        //public OnItemClickListener listener;

        public MyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.listText);
            locationPhoto = (ImageView) itemView.findViewById(R.id.listIcon);
            //this.listener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (!title.getText().toString().equals("There are no favourites to display")) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("placeReference", reference);
                Log.i(TAG, "place ref on creation of fragment = " + reference);
                bundle.putString("title", title.getText().toString());
                bundle.putDouble("lat", latitude);
                bundle.putDouble("long", longitude);
                bundle.putBoolean("isFavourited", true);
                bundle.putString("launchedFrom", "Favourite_list");

                //Get icon from ImageView and convert it to a bitmap, then to a byte array
                BitmapDrawable drawable = (BitmapDrawable) locationPhoto.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                DbBitmapUtility bitmapUtility = new DbBitmapUtility();
                byte[] placeImage = bitmapUtility.getBytes(bitmap);
                bundle.putByteArray("placeImage", placeImage);
                Fragment fragment = new FavouriteItemFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.relativeLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }else{
                Toast.makeText(v.getContext(), "Bad user. No touchey.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public boolean onLongClick(View v){
            Log.i(TAG, "Long clicked");
            return true;
        }

        public void setParams(String reference, Double latitude, Double longitude){
            this.reference=reference;
            this.latitude=latitude;
            this.longitude=longitude;
        }
    }


    /*public interface OnItemClickListener{
        void onItemClick(String textName);
    }*/

}
