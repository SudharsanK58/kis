package com.sudharsan.kis;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private List<String> urlList = new ArrayList<>();
    private ImageLoader imageLoader;
    public ImageAdapter(Context context){
        this.context = context;
        imageLoader = WebOperation.getInstance(context).getImageLoader();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_images,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.networkImageView.setImageUrl(urlList.get(position),imageLoader);
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    public void addAll(List<String> list){
        urlList.clear();
        urlList.addAll(list);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView networkImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            networkImageView = itemView.findViewById(R.id.networkImageView);
        }

    }
}
