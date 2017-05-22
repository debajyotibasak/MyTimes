package com.example.droiddebo.mytimes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{

    private ArrayList<Article> articles;
    private Context context;

    public DataAdapter(Context context, ArrayList<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        holder.tv_card_main_title.setText(articles.get(position).getTitle());
        Picasso.with(context)
                .load(articles.get(position).getUrlToImage())
                .resize(100,100)
                .centerCrop()
                .error(R.drawable.ic_image)
                .into(holder.img_card_main);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_card_main_title;
        private ImageView img_card_main;

        public ViewHolder(View view) {
            super(view);

            tv_card_main_title = (TextView) view.findViewById(R.id.tv_card_main_title);
            img_card_main = (ImageView) view.findViewById(R.id.img_card_main);
        }
    }
}
