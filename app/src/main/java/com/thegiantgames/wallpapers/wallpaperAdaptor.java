package com.thegiantgames.wallpapers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thegiantgames.wallpapers.R;

import java.util.List;

public class wallpaperAdaptor extends RecyclerView.Adapter<wallpaperViewHolder> {
    public wallpaperAdaptor(Context context, List<wallpaperModel> wallpaperModelList) {
        this.context = context;
        this.wallpaperModelList = wallpaperModelList;
    }

    private Context context;
    private List<wallpaperModel> wallpaperModelList;

    @NonNull
    @Override
    public wallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item ,parent ,false);
        return new wallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull wallpaperViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(wallpaperModelList.get(position).getMediumurl()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context ,fullscreenWallpaper.class).putExtra("originalurl" , wallpaperModelList.get(position).getOriginalurl()));

            }
        });
    }

    @Override
    public int getItemCount() {

        return wallpaperModelList.size();
    }
}

class wallpaperViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;

    public wallpaperViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.ImageView);
    }
}