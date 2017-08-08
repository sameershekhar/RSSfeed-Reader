package com.example.sameershekhar.rssreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sameershekhar on 08-Aug-17.
 */

public class MyAdaptor extends RecyclerView.Adapter<MyAdaptor.MyViewHolder> {
    Context context;
    ArrayList<FeedItem> feedItems;
    public MyAdaptor(Context context,ArrayList<FeedItem> feedItems) {
        this.context=context;
        this.feedItems=feedItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_row,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        FeedItem feedItem=feedItems.get(position);
        holder.title.setText(feedItem.getTitle());



    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,description;


        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);

        }
    }
}
