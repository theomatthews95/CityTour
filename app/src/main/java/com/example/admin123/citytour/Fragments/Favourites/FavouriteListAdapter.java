package com.example.admin123.citytour.Fragments.Favourites;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin123.citytour.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by theom on 15/03/2017.
 */

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.MyViewHolder> {

    private List<FavouriteListItem> data = Collections.emptyList();

    public FavouriteListAdapter(List<FavouriteListItem> data){
        this.data=data;

        Log.i("Favourite_List_Adapter","Hi constructor");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View v = inflater.inflate(R.layout.favourite_list_item, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(v, new OnItemClickListener() {
            @Override
            public void onItemClick(String textName) {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FavouriteListItem current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView title;
        ImageView icon;
        String reference;
        Double latitude;
        Double longitude;

        public OnItemClickListener listener;

        public MyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(title.getText().toString());
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Bundle bundle = new Bundle();
            bundle.putString("placeReference",reference);
            Log.i(TAG, "place ref on creation of fragment = "+reference);
            bundle.putString("title", title.getText().toString());
            bundle.putDouble("lat", latitude);
            bundle.putDouble("long", longitude);
            bundle.putBoolean("isFavourited", true);
            Fragment fragment = new FavouriteItemDetailsFragment();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.relativeLayout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
        public void setParams(String reference, Double latitude, Double longitude){
            this.reference=reference;
            this.latitude=latitude;
            this.longitude=longitude;
        }
    }


    public interface OnItemClickListener{
        void onItemClick(String textName);
    }
}
