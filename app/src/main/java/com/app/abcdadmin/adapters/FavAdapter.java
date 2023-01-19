package com.app.abcdadmin.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.abcdadmin.OnSelectedListener;
import com.app.abcdadmin.R;
import com.app.abcdadmin.models.Messages;

import java.util.ArrayList;
import java.util.List;


public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    Activity activity;
    ArrayList<Messages> messages;
    private List<String> mData;
    private OnSelectedListener onSelectedListener;


//    public FavAdapter(ArrayList<Messages> messages, Activity activity) {
//        this.messages = messages;
//        this.activity = activity;
//    }


    public FavAdapter(List<String> data, OnSelectedListener onSelectedListener) {
        this.mData = data;
        this.onSelectedListener = onSelectedListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.fav_message_lyt, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.message.setText(mData.get(position));
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectedListener.onSuccess(mData.get(position));
            }
        });

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView message;

        public ViewHolder(View itemView) {
            super(itemView);
            this.message = (TextView) itemView.findViewById(R.id.message);
        }
    }
}
