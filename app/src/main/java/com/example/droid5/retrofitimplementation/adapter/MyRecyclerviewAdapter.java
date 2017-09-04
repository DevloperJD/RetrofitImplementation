package com.example.droid5.retrofitimplementation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.droid5.retrofitimplementation.R;
import com.example.droid5.retrofitimplementation.model.Video;

import java.util.List;

/**
 * Created by droid5 on 1/9/17.
 */

public class MyRecyclerviewAdapter extends RecyclerView.Adapter<MyRecyclerviewAdapter.holder> {
    private List<Video> videoList;
    private Context mcontext;

    public MyRecyclerviewAdapter(List<Video> videoList, Context mcontext) {
        this.videoList = videoList;
        this.mcontext = mcontext;
    }

    public static class holder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.title);


        }
    }



    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new holder(itemview);
    }

    @Override
    public void onBindViewHolder(holder holder, int position) {
        Video video = videoList.get(position);
        holder.textView.setText(video.getTitle());
        Glide.with(mcontext).load(videoList.get(position).getThumbUrl()).into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }


}
