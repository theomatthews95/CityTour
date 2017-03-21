package com.example.admin123.citytour.Fragments.Favourites;

import android.content.Context;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
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
    private MyViewHolder.OnItemClickListener clickListener;
    private boolean isItinerary;

    public FavouriteListAdapter(List<FavouriteListItem> data, MyViewHolder.OnItemClickListener clickListener, boolean isItinerary){
        this.data=data;
        this.clickListener=clickListener;
        this.isItinerary=isItinerary;
        Log.i("Favourite_List_Adapter","Hi constructor");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        if (isItinerary == false) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_list_item, parent, false);
        }else {
            if (viewType == R.layout.itinerary_info_item) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_list_item, parent, false);
            } else {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_info_item, parent, false);
            }
        }
        MyViewHolder holder = new MyViewHolder(v,clickListener);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FavouriteListItem current = data.get(position);
        holder.title.setText(current.title);
        holder.locationPhoto.setImageBitmap(new DbBitmapUtility().getImage(current.locationPhoto));
        holder.setParams(current.reference, current.latitude, current.longitude);
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        holder.visitedOverlay.setVisibility(current.isVisited  ? View.VISIBLE : View.INVISIBLE);
        holder.timeToNext.setText(current.timeToNext);
        holder.distanceToNext.setText(current.distanceToNext);
    }

    @Override
    public int getItemViewType (int position) {
        //Some logic to know which type will come next;
        if ( (position & 1) == 0 ) { return R.layout.itinerary_info_item; } else { return R.layout.itinerary_list_item; }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ArrayList<FavouriteListItem> getItemsDetails(List<Integer> positions){
        ArrayList<FavouriteListItem> itineraryList = new ArrayList<>();
        for (Integer position : positions){
            itineraryList.add(data.get(position));
        }
        return itineraryList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        CardView cv;
        TextView title;
        ImageView locationPhoto;
        String reference;
        Double latitude;
        Double longitude;
        View selectedOverlay;
        TextView visitedOverlay;
        TextView distanceToNext;
        TextView timeToNext;

        public OnItemClickListener listener;

        public MyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.listText);
            locationPhoto = (ImageView) itemView.findViewById(R.id.listIcon);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);
            visitedOverlay = (TextView) itemView.findViewById(R.id.visited_overlay);
            distanceToNext = (TextView) itemView.findViewById(R.id.distance);
            timeToNext = (TextView) itemView.findViewById(R.id.timing);

            this.listener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
           if (!title.getText().toString().equals("There are no favourites to display"))  {
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
            if (listener != null) {
                listener.onItemClicked(getPosition());
            }

        }

        @Override
        public boolean onLongClick(View v){
            if (listener != null) {
                return listener.onItemLongClicked(getPosition());
            }
            return false;
        }

        public void setParams(String reference, Double latitude, Double longitude){
            this.reference=reference;
            this.latitude=latitude;
            this.longitude=longitude;
        }

        public interface OnItemClickListener{
            public void onItemClicked(int position);
            public boolean onItemLongClicked(int position);
        }

    }



}